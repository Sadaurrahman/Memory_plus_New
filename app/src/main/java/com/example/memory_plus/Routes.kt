package com.example.memory_plus

sealed class Routes(val route: String) {
    object Login : Routes("Login")
    object Signup : Routes("Signup")
    object Dashboard : Routes("dashboard")
}