package com.example.expensetrack;

import android.app.Application
import androidx.room.Room
import com.example.expensetrack.data.repository.DataRepository
import com.example.expensetrack.data.repository.UserRepository
import com.example.expensetrack.data.source.local.AppDatabase
import com.example.expensetrack.di.DependencyInjector
import com.example.expensetrack.di.DependencyInjectorImpl
import com.example.expensetrack.di.Provider


class ExpenseTrackApplication : Application(), Provider {
    override val provider: DependencyInjector = DependencyInjectorImpl()

    override fun onCreate() {
        super.onCreate()
        provideDependencies()
    }

    private fun provideDependencies() {
        provider[AppDatabase::class.java] = Room.databaseBuilder(
            this, AppDatabase::class.java, getString(R.string.app_name)
        ).build()
        provider[DataRepository::class.java] = DataRepository(provider[AppDatabase::class.java]!!)
        provider[UserRepository::class.java] = UserRepository(this)
    }
}