package edu.self.cryptotechnicaltest.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.self.cryptotechnicaltest.core.database.dao.CurrencyInfoDao
import edu.self.cryptotechnicaltest.core.event.Event
import edu.self.cryptotechnicaltest.core.event.sendEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DemoViewModel(
    private val currencyInfoDao: CurrencyInfoDao
) : ViewModel() {

    val actionOfInit: LiveData<Event<Unit>>
        get() = _actionOfInit
    private val _actionOfInit = MutableLiveData<Event<Unit>>()

    init {
        viewModelScope.launch {
            _actionOfInit.sendEvent()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val listOfCurrencyInfo = withContext(Dispatchers.IO) {
                currencyInfoDao.getListOfCurrencyInfo()
            }
        }
    }

    fun sort() {
        viewModelScope.launch {

        }
    }
}