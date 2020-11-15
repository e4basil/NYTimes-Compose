package com.interview.thenewyorktimes.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.interview.thenewyorktimes.R
import com.interview.thenewyorktimes.utility.isDarkThemeOn
import com.interview.thenewyorktimes.utility.setUpStatusNavigationBarColors
import com.interview.thenewyorktimes.utility.setupTheme
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setUpStatusNavigationBarColors(
            isDarkThemeOn(),
            ContextCompat.getColor(this, R.color.background)
        )
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance())
                .commitNow()
        }
        back.setOnClickListener { finish() }
        sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key?.equals("list_theme") == true) {
            sharedPreferences?.setupTheme(key, resources)
            window.setUpStatusNavigationBarColors(
                isDarkThemeOn(),
                ContextCompat.getColor(this, R.color.background)
            )
        }

    }

}