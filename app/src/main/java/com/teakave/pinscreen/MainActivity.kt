package com.teakave.pinscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teakave.pinscreen.ui.pin.PinFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PinFragment.newInstance())
                .commitNow()
        }
    }
}