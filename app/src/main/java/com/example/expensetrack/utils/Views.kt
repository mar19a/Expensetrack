package com.example.expensetrack.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

val Context.activity: AppCompatActivity?
    get() = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.activity
        else -> null
    }