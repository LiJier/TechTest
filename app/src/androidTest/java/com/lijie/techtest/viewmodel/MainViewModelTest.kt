package com.lijie.techtest.viewmodel

import androidx.test.platform.app.InstrumentationRegistry
import com.lijie.techtest.MyApp
import com.lijie.techtest.http.ApiClient
import com.lijie.techtest.repository.MainRepository
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        ApiClient.init("https://data.gov.sg/api/")
        MyApp.appContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun getAmountData() {
        val viewModel = MainViewModel(MainRepository)
        runBlocking(TestCoroutineDispatcher()) {
            joinAll(viewModel.getAmountData())
            TestCase.assertEquals(44, viewModel.amountDataListLiveData.data?.size)
        }
    }

    @Test
    fun getYearAmountData() {
        val viewModel = MainViewModel(MainRepository)
        runBlocking(TestCoroutineDispatcher()) {
            joinAll(viewModel.getAmountData())
            val yearAmountDataList =
                viewModel.getYearAmountData(viewModel.amountDataListLiveData.data.orEmpty())
            TestCase.assertEquals(11, yearAmountDataList.size)
        }

    }

    @Test
    fun getCache() {
        val viewModel = MainViewModel(MainRepository)
        runBlocking(TestCoroutineDispatcher()) {
            joinAll(viewModel.getAmountData())
            TestCase.assertEquals(44, viewModel.getCache().size)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

}