package app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import features.screens.HomeScreen

@Composable
fun App() = MaterialTheme { HomeScreen() }