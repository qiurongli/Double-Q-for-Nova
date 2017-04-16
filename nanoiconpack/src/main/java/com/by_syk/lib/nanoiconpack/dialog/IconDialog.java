/*
 * Copyright 2017 By_syk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.by_syk.lib.nanoiconpack.dialog;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.by_syk.lib.nanoiconpack.R;
import com.by_syk.lib.nanoiconpack.bean.IconBean;
import com.by_syk.lib.nanoiconpack.util.AppFilterReader;
import com.by_syk.lib.nanoiconpack.util.C;
import com.by_syk.lib.nanoiconpack.util.ExtraUtil;
import com.by_syk.lib.nanoiconpack.util.PkgUtil;
import com.by_syk.lib.toast.GlobalToast;

import java.util.List;

/**
 * Created by By_syk on 2017-01-27.
 */

public class IconDialog extends DialogFragment {
    private View iconGridView;
    private View iconViewSmall;

    private IconBean iconBean;

    private boolean isAppInstalled = true;

    private boolean isExecuted = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View viewContent = getActivity().getLayoutInflater().inflate(R.layout.dialog_icon, null);

        iconViewSmall = viewContent.findViewById(R.id.small_icon_view);
        iconViewSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconViewSmall.setVisibility(View.GONE);
                iconGridView.setVisibility(View.INVISIBLE);
            }
        });

        iconGridView = viewContent.findViewById(R.id.icon_grid);

        ImageView ivIcon = (ImageView) viewContent.findViewById(R.id.iv_icon);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iconGridView.getVisibility() == View.VISIBLE) {
                    iconGridView.setVisibility(View.INVISIBLE);
                } else {
                    iconGridView.setVisibility(View.VISIBLE);
                }
                if (!isAppInstalled) {
                    return;
                }
                if (iconViewSmall == null) {
                    (new ExtractRawIconTask()).execute();
                } else {
                    if (iconViewSmall.getVisibility() == View.VISIBLE) {
                        iconViewSmall.setVisibility(View.GONE);
                    } else {
                        iconViewSmall.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        ivIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                saveIcon();
                return true;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(viewContent);

        Bundle bundle = getArguments();
        if (bundle != null) {
            iconBean = (IconBean) bundle.getSerializable("bean");
            if (iconBean != null) {
                builder.setTitle(iconBean.getLabel() != null ? iconBean.getLabel() : iconBean.getName());
//                ivIcon.setImageResource(iconBean.getId());
                int hdIconId = getResources().getIdentifier(iconBean.getName(), "mipmap",
                        getContext().getPackageName());
                if (hdIconId != 0) {
                    ivIcon.setImageResource(hdIconId);
                } else {
                    ivIcon.setImageResource(iconBean.getId());
                }
            }
            if (bundle.getBoolean("pick")) {
                builder.setPositiveButton(R.string.dlg_bt_pick, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        returnPickIcon();
                    }
                });
            }
        }

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!isExecuted) {
            isExecuted = true;

            (new ExtractRawIconTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    "extractRawIconTask");

            // 浮入浮出动画
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setWindowAnimations(android.R.style.Animation_InputMethod);
            }
        }
    }

    @TargetApi(23)
    private void saveIcon() {
        if (C.SDK >= 23 && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }

        boolean ok = ExtraUtil.saveIcon(getContext(), iconBean);
        GlobalToast.showToast(getContext(), ok ? R.string.toast_icon_saved
                : R.string.toast_icon_save_failed);
    }

    private void returnPickIcon() {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(getResources(), iconBean.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        if (bitmap != null) {
            intent.putExtra("icon", bitmap);
            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", iconBean.getId());
            intent.setData(Uri.parse("android.resource://" + getContext().getPackageName()
                    + "/" + String.valueOf(iconBean.getId())));
            getActivity().setResult(Activity.RESULT_OK, intent);
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED, intent);
        }
        getActivity().finish();
    }

    class ExtractRawIconTask extends AsyncTask<String, Boolean, Drawable> {
        @Override
        protected Drawable doInBackground(String... strings) {
            if (!isAdded()) {
                return null;
            }

//            List<String> matchedPkgList = ExtraUtil.getAppFilterPkg(getResources(), iconBean.getName());
//            for (String pkgName : matchedPkgList) {
//                if (PkgUtil.isPkgInstalled(getContext(), pkgName)) {
//                    PackageManager packageManager = getContext().getPackageManager();
//                    try {
//                        PackageInfo packageInfo = packageManager.getPackageInfo(pkgName, 0);
//                        return packageInfo.applicationInfo.loadIcon(packageManager);
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
            AppFilterReader reader = AppFilterReader.getInstance();
            reader.init(getResources());
            List<AppFilterReader.Bean> matchedList = reader.findByDrawable(iconBean.getName());

            if (!isAdded()) {
                return null;
            }

            boolean use = false;
            for (AppFilterReader.Bean bean : matchedList) {
                if (iconBean.getName().equals(bean.drawable)) {
                    use = true;
                    break;
                }
            }
            publishProgress(use);

            PackageManager packageManager = getContext().getPackageManager();
            for (AppFilterReader.Bean bean : matchedList) {
                if (bean.pkg == null || bean.launcher == null) { // invalid
                    continue;
                }
//                Drawable icon = PkgUtil.getIcon(packageManager, bean.pkg);
                Drawable icon = PkgUtil.getIcon(packageManager, bean.pkg, bean.launcher);
                if (icon != null) {
                    return icon;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);

            if (isAdded() && values[0]) {
                getDialog().setTitle(iconBean.getLabel() + C.ICON_ONE_SUFFIX);
            }
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);

            if (!isAdded()) {
                return;
            }

            if (drawable == null) {
                isAppInstalled = false;
                return;
            }

            ((ImageView) iconViewSmall.findViewById(R.id.iv_icon_small)).setImageDrawable(drawable);
            iconViewSmall.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iconGridView.setVisibility(View.VISIBLE);
                    iconViewSmall.setVisibility(View.VISIBLE);
                }
            }, 100);
        }
    }

    public static IconDialog newInstance(IconBean bean, boolean isPick) {
        IconDialog dialog = new IconDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        bundle.putBoolean("pick", isPick);
        dialog.setArguments(bundle);

        return dialog;
    }
}
