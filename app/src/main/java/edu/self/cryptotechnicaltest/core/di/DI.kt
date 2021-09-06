package edu.self.cryptotechnicaltest.core.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.self.cryptotechnicaltest.core.database.AppDatabase
import edu.self.cryptotechnicaltest.currencylist.CurrencyListFragment
import edu.self.cryptotechnicaltest.currencylist.CurrencyListViewModel
import edu.self.cryptotechnicaltest.demo.DemoViewModel
import kotlinx.coroutines.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@DelicateCoroutinesApi
val moduleOfDatabase = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "app-database.db"
        ).addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            //pre-population
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('BTC','Bitcoin','BTC');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('ETH','Ethereum','ETH');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('XRP','XRP','XRP');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('BCH','Bitcoin Cash','BCH');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('LTC','Litecoin','LTC');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('EOS','EOS','EOS');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('BNB','Binance Coin','BNB');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('LINK','Chainlink','LINK');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('NEO','NEO','NEO');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('ETC','Ethereum Classic','ETC');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('ONT','Ontology','ONT');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('CRO','Crypto.com Chain','CRO');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('CUC','Cucumber','CUC');")
                            db.execSQL("INSERT INTO `CURRENCY_INFO`(id,name,symbol) VALUES ('USDC','USD Coin','USDC');")
                        }
                    }
                }
            }
        ).build()
    }
    factory {
        get<AppDatabase>().currencyInfoDao()
    }
}

val moduleOfViewModel = module {
    viewModel {
        DemoViewModel(get())
    }
    viewModel { (request: CurrencyListFragment.Request) ->
        CurrencyListViewModel(request)
    }
}

@DelicateCoroutinesApi
val listOfModule = listOf(
    moduleOfDatabase,
    moduleOfViewModel
)

