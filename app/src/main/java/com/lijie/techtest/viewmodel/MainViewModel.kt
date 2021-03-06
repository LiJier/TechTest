package com.lijie.techtest.viewmodel

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lijie.techtest.MyApp
import com.lijie.techtest.entity.AmountData
import com.lijie.techtest.entity.YearAmountData
import com.lijie.techtest.http.ResLiveData
import com.lijie.techtest.repository.IMainRepository
import kotlinx.coroutines.*

/**
 * ViewModel层，负责业务逻辑，数据传递
 */
class MainViewModel(private val repository: IMainRepository) : ViewModel() {

    private val sp by lazy {
        MyApp.appContext.getSharedPreferences("cache", Context.MODE_PRIVATE)
    }
    private val gson by lazy {
        Gson()
    }
    val amountDataListLiveData by lazy {
        ResLiveData<List<AmountData>>()
    }

    /**
     * 获取使用数据，默认获取2008年-2018年
     */
    fun getAmountData(
        limit: Int = 44,
        offset: Int = 14
    ): Job {
        return launch(amountDataListLiveData) {
            repository.getAmountData(limit, offset).also {
                //成功后缓存数据到本地
                sp.edit {
                    putString("getAmountData:limit$limit&offset$offset", gson.toJson(it.orEmpty()))
                }
            }
        }
    }

    /**
     * 获取按年分组数据，并找出有季度数据量下降的年份
     */
    fun getYearAmountData(amountDataList: List<AmountData>): List<YearAmountData> {
        val yearAmountDataMap = HashMap<Int, List<AmountData>>()
        amountDataList.forEach {
            val year = it.quarter?.split("-")?.getOrElse(0) { "2008" }.toString().toInt()
            val dataList = if (yearAmountDataMap.containsKey(year)) {
                yearAmountDataMap[year] as ArrayList
            } else {
                arrayListOf()
            }
            dataList.add(it)
            yearAmountDataMap[year] = dataList
        }
        val yearAmountDataList = arrayListOf<YearAmountData>()
        yearAmountDataMap.entries.forEach { mutableEntry ->
            val dataList = mutableEntry.value
            dataList.sortedBy {
                it.quarter
            }
            var hasDecline = false
            dataList.forEachIndexed { index, amountData ->
                if (index < dataList.size - 1) {
                    val next = dataList[index + 1]
                    if (next.volumeOfMobileData < amountData.volumeOfMobileData) {
                        hasDecline = true
                        return@forEachIndexed
                    }
                }
            }
            yearAmountDataList.add(YearAmountData(mutableEntry.key, dataList, hasDecline))
        }
        return yearAmountDataList.sortedBy {
            it.year
        }
    }

    /**
     * 获取本地缓存
     */
    fun getCache(
        limit: Int = 44,
        offset: Int = 14
    ): List<AmountData> {
        val jsonString = sp.getString("getAmountData:limit$limit&offset$offset", "")
        return gson.fromJson(jsonString, object : TypeToken<List<AmountData>>() {}.type)
    }

    //使用协程获取数据,并通过LiveData发送
    private inline fun <T> launch(
        resLiveData: ResLiveData<T>? = null,
        crossinline block: suspend () -> T?
    ): Job {
        return viewModelScope.launch {
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