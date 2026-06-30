package com.userexplorer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.userexplorer.app.navigation.UserExplorerNavGraph
import com.userexplorer.app.ui.theme.UserExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application.
 * Handles the initial setup and content hosting.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserExplorerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserExplorerNavGraph()
                }
            }
        }
    }
}