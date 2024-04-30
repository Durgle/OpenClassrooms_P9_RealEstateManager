package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.estate.EstateFragment.Companion.newInstance

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.getBoolean(R.bool.isLargeLayout)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, newInstance())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, newInstance())
                .commit()
        }
    }
}
