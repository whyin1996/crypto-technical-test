package edu.self.cryptotechnicaltest.core.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "CURRENCY_INFO")
data class CurrencyInfo(
    @ColumnInfo(name = "ID")
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "NAME")
    val name: String,
    @ColumnInfo(name = "SYMBOL")
    val symbol: String
) : Parcelable