package com.example.expensetrack.utils

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
private val currencyFormatter = DecimalFormat("#,###.00")

fun LocalDate.toText(): String = format(dateFormatter)

val Float.formatted: String
    get() = currencyFormatter.format(this)

val String.float: Float?
    get() = replace(",", "").toFloatOrNull()

fun Float.currency(currency: String = "$") = "$currency$formatted"