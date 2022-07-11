package com.solmi.biobrainexample.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.PreferenceScreen
import android.widget.BaseAdapter
import android.widget.ListPopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.database.core.SyncTree
import com.solmi.biobrainexample.R
import java.util.prefs.PreferencesFactory

class Settings : PreferenceFragment() {

    lateinit var prefs : SharedPreferences
    lateinit var soundPreference : ListPreference
    lateinit var keywordSoundPreferences: ListPreference
    lateinit var keywordScreen : PreferenceScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.setting_preference)
        soundPreference = findPreference("sound_list") as ListPreference
        keywordSoundPreferences = findPreference("keyword_sound_list") as ListPreference
        keywordScreen = findPreference("keyword_screen") as PreferenceScreen

        prefs = PreferenceManager.getDefaultSharedPreferences(activity) as SharedPreferences

        if(!prefs.getString("sound_list", "").equals("")){
            soundPreference.summary = prefs.getString("sound_list","카톡")
        }

        if(!prefs.getString("keyword_sound_list", "").equals("")){
            keywordSoundPreferences.summary = prefs.getString("keyword_sound_list","카톡k")
        }

        if(prefs.getBoolean("keyword",false)){
            keywordScreen.summary = "사용"
        }
        //리스너 연결
        prefs.registerOnSharedPreferenceChangeListener(prefListener)

    }

    val prefListener = object : SharedPreferences.OnSharedPreferenceChangeListener{
        override fun onSharedPreferenceChanged(sharedPreferences : SharedPreferences?, key: String?) {
            if(key.equals("sound_list")){
                soundPreference.summary = prefs.getString("sound_list","카톡")
            }

            if(key.equals("keyword_sound_list")){
                keywordSoundPreferences.summary = prefs.getString("keyword_sound_list","카톡k")
            }

            if(key.equals("keyword")){
                if(prefs.getBoolean("keyword",false)){
                    keywordScreen.summary = "사용"
                }else{
                    keywordScreen.summary = "사용안함"
                }
            }
            //2뎁스 preference screen 내부에서 발생한 환경설정 내용을 2뎁스 preferenceScreen 에 적용
            (preferenceScreen.rootAdapter as BaseAdapter).notifyDataSetChanged()
        }

    }
}