package com.solmi.biobrainexample.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toolbar
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.databinding.ActivityFcmTestMainBinding.inflate

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_setting)

        val topToolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.top_toolbar)
        setSupportActionBar(topToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.setting_title)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, NestedSettingPreferenceFragment(), "setting_fragment")
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}