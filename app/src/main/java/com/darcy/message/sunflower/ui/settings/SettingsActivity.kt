package com.darcy.message.sunflower.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.darcy.message.sunflower.R
import com.darcy.message.sunflower.ui.settings.fragment.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance("Settings"))
                .commit()
        }
    }
}