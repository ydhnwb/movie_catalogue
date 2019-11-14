package com.ydhnwb.moviecatalogue.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ydhnwb.moviecatalogue.R
import com.ydhnwb.moviecatalogue.utilities.Constants
import android.content.Intent
import android.provider.Settings
import com.ydhnwb.moviecatalogue.services.DailyReminderReceiver


class SettingFragment : PreferenceFragmentCompat(){
    private lateinit var preferenceListener : SharedPreferences.OnSharedPreferenceChangeListener
    private var dailyReminder = DailyReminderReceiver()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferenceScreen.findPreference<Preference>(Constants.PREF_LANGUAGE)!!.setOnPreferenceClickListener {pref ->
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            return@setOnPreferenceClickListener true
        }
        preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener{sharedPref, key ->
            if(key.equals(Constants.PREF_DAILY_REMINDER)){
                handleAlarm(sharedPref.getBoolean(Constants.PREF_DAILY_REMINDER, true))
                handleDailyReminder()
            }else if(key.equals(Constants.PREF_RELEASE_REMINDER)){
                handleReleaseReminder()
            }
        }
    }

    private fun handleAlarm(statae : Boolean){
        if(statae){
            dailyReminder.setAlarm(activity!!)
        }else{
            dailyReminder.cancelAlarm(activity!!)
        }
    }

    private fun handleDailyReminder(){
        findPreference<Preference>(Constants.PREF_DAILY_REMINDER)!!
            .setDefaultValue(preferenceScreen.sharedPreferences.getBoolean(Constants.PREF_DAILY_REMINDER, true))
    }

    private fun handleReleaseReminder(){
        findPreference<Preference>(Constants.PREF_RELEASE_REMINDER)!!
            .setDefaultValue(preferenceScreen.sharedPreferences.getBoolean(Constants.PREF_RELEASE_REMINDER, true))
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener)
        handleDailyReminder()
        handleReleaseReminder()
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceListener)
    }

}