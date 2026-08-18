package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.MainScreen
import com.example.ui.theme.WingsResortTheme
import com.example.ui.viewmodel.ResortViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ResortViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WingsResortTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                Crossfade(
                    targetState = state.isAuthenticated,
                    animationSpec = tween(400),
                    label = "AuthCrossfade",
                    modifier = Modifier.fillMaxSize()
                ) { authenticated ->
                    if (authenticated) {
                        MainScreen(
                            viewModel = viewModel,
                            state = state
                        )
                    } else {
                        AuthScreen(
                            isLoading = state.isAuthLoading,
                            errorMessage = state.authError,
                            onLogin = { email, pass -> viewModel.login(email, pass) },
                            onDemoLogin = { role -> viewModel.loginDemo(role) }
                        )
                    }
                }
            }
        }
    }
}
