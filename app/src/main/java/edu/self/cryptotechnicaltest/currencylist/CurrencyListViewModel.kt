package edu.self.cryptotechnicaltest.currencylist

import androidx.lifecycle.*
import edu.self.cryptotechnicaltest.core.database.model.CurrencyInfo
import edu.self.cryptotechnicaltest.core.event.Event
import edu.self.cryptotechnicaltest.core.event.sendEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext

class CurrencyListViewModel(private val request: CurrencyListFragment.Request) : ViewModel() {

    private data class DataSource(
        val listOfCurrencyInfo: List<CurrencyInfo>
    )

    private val _dataSource = MutableLiveData(DataSource(listOf()))

    //deal with ​concurrency
    //issue​ when do sorting (fast double click of sorting button)
    //using semaphore to ensure the dataSource, actually is list of currency info in this case
    //can only be access by ONE process (or so-called thread) in the same time
    //if there is a process accessing the data source
    //another process would be sleep and do the rest process
    //when the process accessing before is completed its access
    private val semaphore = Semaphore(1)

    val actionOfCommit: LiveData<Event<Pair<CurrencyListFragment.Request, CurrencyListFragment.Response>>>
        get() = _actionOfCommit
    private val _actionOfCommit =
        MutableLiveData<Event<Pair<CurrencyListFragment.Request, CurrencyListFragment.Response>>>()

    val listOfVM = _dataSource.switchMap {
        liveData(Dispatchers.IO) {
            emit(it.listOfCurrencyInfo.map {
                val prefix = runCatching {
                    require(it.name.isNotBlank())
                    it.name.first().toString()
                }.getOrElse { "-" }
                CurrencyListAdapter.CurrencyInfoModel(
                    id = it.id,
                    prefix = prefix,
                    name = it.name,
                    symbol = it.symbol
                )
            })
        }
    }

    fun sort() {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    semaphore.withPermit {
                        val snapshot = _dataSource.value!!
                        //All the IO operations ​MUST NOT ​be in UI Thread.
                        //sorting using background thread
                        val listOfInfo = snapshot.listOfCurrencyInfo.sortedBy {
                            it.id
                        }
                        withContext(Dispatchers.Main) {
                            _dataSource.value = snapshot.copy(
                                listOfCurrencyInfo = listOfInfo
                            )
                        }
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun loadData(listOfCurrencyInfo: List<CurrencyInfo>) {
        viewModelScope.launch {
            //CurrencyListFragment should receive an array list of CurrencyInfo to create the ui.
            runCatching {
                semaphore.withPermit {
                    val snapshot = _dataSource.value!!
                    withContext(Dispatchers.Main) {
                        _dataSource.value = snapshot.copy(
                            listOfCurrencyInfo = listOfCurrencyInfo
                        )
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun onAdapterDelegate(position: Int) {
        viewModelScope.launch {
            val targetModel =
                _dataSource.value?.listOfCurrencyInfo?.getOrNull(position) ?: return@launch
            _actionOfCommit.sendEvent(request to CurrencyListFragment.Response(targetModel))
        }
    }
}