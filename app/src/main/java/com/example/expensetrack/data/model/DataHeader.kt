package com.example.expensetrack.data.model

import java.util.*


data class DataHeader(
    val value: String, val sortType: SortType
) {
    enum class SortType {
        Up, Down, None;

        fun switch(): SortType {
            return when (this) {
                Up -> Down
                Down -> None
                None -> Up
            }
        }
    }
}

val DataHeader.title: String
    get() {
        return value.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }