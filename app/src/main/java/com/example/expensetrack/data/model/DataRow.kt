package com.example.expensetrack.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetrack.utils.currency
import com.example.expensetrack.utils.toText
import java.time.LocalDate

@Entity(tableName = DataRow.TableName)
data class DataRow(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val category: String,
    val total: Float,
    val status: String,
    val comment: String,
    val receipt: String?,
) {
    val items: List<String>
        get() {
            return listOf(
                date.toText(),
                category,
                total.currency(),
                status,
                comment,
            )
        }

    companion object {
        const val TableName = "data"
    }
}
