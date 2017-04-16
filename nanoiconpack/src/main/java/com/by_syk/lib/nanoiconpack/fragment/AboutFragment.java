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

package com.by_syk.lib.nanoiconpack.fragment;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;

import com.by_syk.lib.nanoiconpack.R;
import com.by_syk.lib.nanoiconpack.dialog.QrcodeDialog;
import com.by_syk.lib.nanoiconpack.util.PkgUtil;
import com.by_syk.lib.text.AboutMsgRender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by By_syk on 2017-02-17.
 */

public class AboutFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private static final String PREFERENCE_ICONS = "icons";
    private static final String PREFERENCE_ICONS_NOTE = "iconsNote";
    private static final String PREFERENCE_ICONS_AUTHOR = "iconsAuthor";
    private static final String PREFERENCE_ICONS_CONTACT = "iconsContact";
    private static final String PREFERENCE_ICONS_DONATE = "iconsDonate";
    private static final String PREFERENCE_ICONS_TODO_1 = "iconsTodo1";
    private static final String PREFERENCE_ICONS_COPYRIGHT = "iconsCopyright";
    private static final String PREFERENCE_APP = "app";
    private static final String PREFERENCE_APP_APP = "appApp";
    private static final String PREFERENCE_APP_TODO_1 = "appTodo1";
    private static final String PREFERENCE_APP_DASHBOARD = "appDashboard";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_about);

        init();
    }

    private void init() {
        PreferenceCategory prefCatIcons = (PreferenceCategory) findPreference(PREFERENCE_ICONS);
        Preference prefIconsNote = findPreference(PREFERENCE_ICONS_NOTE);
        Preference prefIconsAuthor = findPreference(PREFERENCE_ICONS_AUTHOR);
        Preference prefIconsContact = findPreference(PREFERENCE_ICONS_CONTACT);
        Preference prefIconsDonate = findPreference(PREFERENCE_ICONS_DONATE);
        Preference prefIconsTodo1 = findPreference(PREFERENCE_ICONS_TODO_1);
        Preference prefIconsCopyright = findPreference(PREFERENCE_ICONS_COPYRIGHT);
        PreferenceCategory prefCatApp = (PreferenceCategory) findPreference(PREFERENCE_APP);
        Preference prefAppApp = findPreference(PREFERENCE_APP_APP);
        Preference prefAppTodo1 = findPreference(PREFERENCE_APP_TODO_1);
        Preference prefAppDashboard = findPreference(PREFERENCE_APP_DASHBOARD);

//        prefIconsNote.setOnPreferenceClickListener(this);
        prefIconsAuthor.setOnPreferenceClickListener(this);
        prefIconsContact.setOnPreferenceClickListener(this);
        prefIconsDonate.setOnPreferenceClickListener(this);
        prefIconsTodo1.setOnPreferenceClickListener(this);
        prefIconsCopyright.setOnPreferenceClickListener(this);
        prefAppApp.setOnPreferenceClickListener(this);
        prefAppTodo1.setOnPreferenceClickListener(this);
        prefAppDashboard.setOnPreferenceClickListener(this);

        prefCatIcons.setTitle(getString(R.string.preference_category_icons,
                getResources().getStringArray(R.array.icons).length));

        String summary = AboutMsgRender.parseCode(getString(R.string.preference_icons_summary_author));
        if (!TextUtils.isEmpty(summary)) {
            prefIconsAuthor.setSummary(summary);
        }
        summary = AboutMsgRender.parseCode(getString(R.string.preference_icons_summary_contact));
        if (!TextUtils.isEmpty(summary)) {
            prefIconsContact.setSummary(summary);
        }
        summary = AboutMsgRender.parseCode(getString(R.string.preference_icons_summary_donate));
        if (!TextUtils.isEmpty(summary)) {
            prefIconsDonate.setSummary(summary);
        }
        summary = AboutMsgRender.parseCode(getString(R.string.preference_icons_summary_todo_1));
        if (!TextUtils.isEmpty(summary)) {
            prefIconsTodo1.setSummary(summary);
        }
        summary = AboutMsgRender.parseCode(getString(R.string.preference_icons_summary_copyright));
        if (!TextUtils.isEmpty(summary)) {
            prefIconsCopyright.setSummary(summary);
        }
        summary = PkgUtil.getAppVer(getContext(), getString(R.string.preference_app_summary_app));
        if (!TextUtils.isEmpty(summary)) {
            prefAppApp.setSummary(summary);
        }
        summary = AboutMsgRender.parseCode(getString(R.string.preference_app_summary_todo_1));
        if (!TextUtils.isEmpty(summary)) {
            prefAppTodo1.setSummary(summary);
        }
        summary = AboutMsgRender.parseCode(getString(R.string.preference_app_summary_dashboard));
        if (!TextUtils.isEmpty(summary)) {
            prefAppDashboard.setSummary(summary);
        }

        if (prefIconsNote.getSummary() == null || prefIconsNote.getSummary().length() == 0) {
            prefCatIcons.removePreference(prefIconsNote);
        }
        if (prefIconsAuthor.getSummary() == null || prefIconsAuthor.getSummary().length() == 0) {
            prefCatIcons.removePreference(prefIconsAuthor);
        }
        if (prefIconsContact.getSummary() == null || prefIconsContact.getSummary().length() == 0) {
            prefCatIcons.removePreference(prefIconsContact);
        }
        if (prefIconsDonate.getSummary() == null || prefIconsDonate.getSummary().length() == 0) {
            prefCatIcons.removePreference(prefIconsDonate);
        }
        if (prefIconsTodo1.getSummary() == null || prefIconsTodo1.getSummary().length() == 0) {
            prefCatIcons.removePreference(prefIconsTodo1);
        }
        if (prefIconsCopyright.getSummary() == null || prefIconsCopyright.getSummary().length() == 0) {
            prefCatIcons.removePreference(prefIconsCopyright);
        }
        if (prefAppApp.getSummary() == null || prefAppApp.getSummary().length() == 0) {
            prefCatApp.removePreference(prefAppApp);
        }
        if (prefAppTodo1.getSummary() == null || prefAppTodo1.getSummary().length() == 0) {
            prefCatApp.removePreference(prefAppTodo1);
        }
        if (prefAppDashboard.getSummary() == null || prefAppDashboard.getSummary().length() == 0) {
            prefCatApp.removePreference(prefAppDashboard);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
//            case PREFERENCE_ICONS_NOTE:
//                break;
            case PREFERENCE_ICONS_AUTHOR:
                executeCode(preference.getTitle().toString(),
                        getString(R.string.preference_icons_summary_author));
                break;
            case PREFERENCE_ICONS_CONTACT:
                executeCode(preference.getTitle().toString(),
                        getString(R.string.preference_icons_summary_contact));
                break;
            case PREFERENCE_ICONS_DONATE:
                executeCode(preference.getTitle().toString(),
                        getString(R.string.preference_icons_summary_donate));
                break;
            case PREFERENCE_ICONS_TODO_1:
                executeCode(preference.getTitle().toString(),
                        getString(R.string.preference_icons_summary_todo_1));
                break;
            case PREFERENCE_ICONS_COPYRIGHT:
                executeCode(preference.getTitle().toString(),
                        getString(R.string.preference_icons_summary_copyright));
                break;
//            case PREFERENCE_APP_APP:
//                executeCode(preference.getTitle().toString(), preference.getSummary().toString());
//                break;
            case PREFERENCE_APP_TODO_1:
                executeCode(preference.getTitle().toString(),
                        getString(R.string.preference_app_summary_todo_1));
                break;
            case PREFERENCE_APP_DASHBOARD:
                executeCode(preference.getTitle().toString(),
                        getString(R.string.preference_app_summary_dashboard));
                break;
        }
        return true;
    }

    private void executeCode(String title, String summary) {
        if (TextUtils.isEmpty(summary)) {
            return;
        }

        Matcher matcher = Pattern.compile("\\[.*?\\]\\(qrcode:(.*?)\\)").matcher(summary);
        if (matcher.find()) {
            QrcodeDialog.newInstance(title, matcher.group(1))
                    .show(getFragmentManager(), "qrcodeDialog");
        } else {
            AboutMsgRender.executeCode(getActivity(), summary);
        }
    }
}
