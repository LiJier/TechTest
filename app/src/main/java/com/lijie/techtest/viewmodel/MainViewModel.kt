package com.lijie.techtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.techtest.entity.AmountData
import com.lijie.techtest.http.ResLiveData
import com.lijie.techtest.repository.IMainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel层，负责业务逻辑，数据传递
 */
class MainViewModel(private val repository: IMainRepository) : ViewModel() {

    val amountDataList by lazy {
        ResLiveData<List<AmountData>>()
    }

    /**
     * 获取使用数据，默认获取2008年-2018年
     */
    fun getAmountData(
        limit: Int = 44,
        offset: Int = 14
    ) {
        launch(amountDataList) {
            repository.getAmountData(limit, offset)
        }
    }

    //使用协程获取数据,并通过LiveData发送
    private inline fun <T> launch(
        resLiveData: ResLiveData<T>? = null,
        crossinline block: suspend () -> T?
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.Main) {
                    resLiveData?.loading(resLiveData.progress)
                    val t = withContext(Dispatchers.IO) {
                        block()
                    }
                    resLiveData?.success(t)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                resLiveData?.error(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}