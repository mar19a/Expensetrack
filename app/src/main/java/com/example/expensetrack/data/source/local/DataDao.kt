package com.example.expensetrack.data.source.local

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.expensetrack.data.model.DataRow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg data: DataRow)

    @Query("SELECT * FROM data")
    suspend fun getAll(): List<DataRow>

    @RawQuery
    suspend fun runtimeQuery(query: SupportSQLiteQuery): List<DataRow>
}