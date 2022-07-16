package com.solmi.biobrainexample.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.solmi.biobrainexample.R

class NestedSettingPreferenceFragment : PreferenceFragmentCompat() {
    lateinit var prefs: SharedPreferences

    private var alarmPreference: Preference? = null
    var alarmSoundListPreference: Preference? = null
    var alarmTypeListPreference: Preference? = null

    // onCreate() 중에 호출되어 Fragment에 preference를 제공하는 메서드
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // preference xml을 inflate하는 메서드
        setPreferencesFromResource(R.xml.setting_preference, rootKey)
        prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        // rootkey가 null이라면
        if (rootKey == null) {
            // Preference 객체들 초기화
            alarmPreference = findPreference("alarm")
            alarmSoundListPreference = findPreference("alarm_sound_list")
            alarmTypeListPreference = findPreference("alarm_type_list")
        }
    }

    // 설정 변경 이벤트 처리
    private val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener {
                sharedPreferences: SharedPreferences?, key: String? ->
            // key는 xml에 등록된 key에 해당
            when (key) {
                "alarm_sound_list" -> {
                    // SharedPreferences에 저장된 값을 가져와서 summary 설정
                    val summary = prefs.getString("alarm_sound_list", "사운드선택")
                    alarmSoundListPreference?.summary = summary
                }
                "alarm_type_list" -> {
                    val summary = prefs.getString("alarm_type_list", "타입선택")
                    alarmTypeListPreference?.summary = summary

                }
            }
        }

    // 리스너 등록
    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    // 리스너 해제
    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener)
    }
}