package edu.self.cryptotechnicaltest.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.self.cryptotechnicaltest.core.database.dao.CurrencyInfoDao
import edu.self.cryptotechnicaltest.core.database.model.CurrencyInfo

@Database(
    entities = [CurrencyInfo::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyInfoDao(): CurrencyInfoDao
}