package edu.self.cryptotechnicaltest.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.self.cryptotechnicaltest.core.database.dao.CurrencyInfoDao
import edu.self.cryptotechnicaltest.core.database.model.CurrencyInfo
import edu.self.cryptotechnicaltest.currencylist.CurrencyListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DemoViewModel(
    private val currencyInfoDao: CurrencyInfoDao
) : ViewModel() {

    val page: LiveData<CurrencyListFragment.Request>
        get() = _page
    private val _page = MutableLiveData<CurrencyListFragment.Request>()

    init {
        viewModelScope.launch {
            _page.value = CurrencyListFragment.Request()
        }
    }

    fun loadData(callback: List<CurrencyInfo>.() -> Unit) {
        viewModelScope.launch {
            //DemoActivity should provide 1 dataset, Currency List A of CurrencyInfo to
            //CurrencyListFragment and the dataset should be queried from local db
            runCatching {
                ////All the IO operations ​MUST NOT ​be in UI Thread.
                //retrieving data from database using background thread
                val listOfCurrencyInfo = withContext(Dispatchers.IO) {
                    currencyInfoDao.getListOfCurrencyInfo()
                }
                callback(listOfCurrencyInfo)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}