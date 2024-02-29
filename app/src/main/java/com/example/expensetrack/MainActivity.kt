package com.example.expensetrack;

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import com.example.expensetrack.ui.NavGraph
import com.example.expensetrack.ui.theme.ExpenseManagerTheme
import com.example.expensetrack.ui.theme.Theme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseManagerTheme {
                val navController = rememberNavController()
                Surface(
                    color = Theme.colors.background,
                    content = {
                        NavGraph(navController = navController)
                    }
                )
            }
        }
    }
}
