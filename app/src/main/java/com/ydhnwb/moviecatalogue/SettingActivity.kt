package com.ydhnwb.moviecatalogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ydhnwb.moviecatalogue.fragments.SettingFragment
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar_setting)
        toolbar_setting.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar_setting.setNavigationOnClickListener { finish() }
        supportActionBar?.title = resources.getString(R.string.title_activity_setting)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().add(R.id.setting_container, SettingFragment()).commit()
        }
    }
}
