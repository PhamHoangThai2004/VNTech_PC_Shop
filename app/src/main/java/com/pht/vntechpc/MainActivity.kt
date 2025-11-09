package com.pht.vntechpc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pht.vntechpc.ui.navigation.RootNavigation
import com.pht.vntechpc.ui.theme.VnTechPCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VnTechPCTheme {
                RootNavigation()
            }
        }
    }
}