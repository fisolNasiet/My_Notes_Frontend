package com.example.mynotesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mynotesapplication.frontend.NoteNavHost
import com.example.mynotesapplication.ui.theme.MyNotesApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyNotesApplicationTheme {
                NoteNavHost()
            }
        }
    }
}
