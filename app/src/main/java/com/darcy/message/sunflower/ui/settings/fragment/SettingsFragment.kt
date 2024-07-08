package com.darcy.message.sunflower.ui.settings.fragment

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.darcy.message.lib_ui.exts.toasts
import com.darcy.message.sunflower.R

class SettingsFragment : PreferenceFragmentCompat() {
    companion object {
        fun newInstance(title: String): SettingsFragment {
            return SettingsFragment().also {
                it.arguments = Bundle().apply { putString("title", title) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_pref_general)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.app_pref_general_key_test_item1) -> {
                // TODO
                toasts("test item1")
            }
            getString(R.string.app_pref_general_key_test_item2) -> {
                // TODO
                toasts("test item2")
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}