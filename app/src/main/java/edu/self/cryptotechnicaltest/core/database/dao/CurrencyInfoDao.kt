package edu.self.cryptotechnicaltest.core.database.dao

import androidx.room.*
import edu.self.cryptotechnicaltest.core.database.model.CurrencyInfo

@Dao
interface CurrencyInfoDao {

    @Query("SELECT * FROM currency_info")
    suspend fun getListOfCurrencyInfo(): List<CurrencyInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    suspend fun insertOrReplaceAllCurrencyInfo(listOfCurrencyInfo: List<CurrencyInfo>)
}