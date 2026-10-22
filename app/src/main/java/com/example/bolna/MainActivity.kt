package com.example.bolna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bolna.core.CustomNavigation
import com.example.bolna.ui.theme.BolNaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BolNaTheme {
                CustomNavigation();
            }
        }
    }
}
