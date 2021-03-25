package com.lijie.techtest.viewmodel

import com.lijie.techtest.http.ApiClient
import com.lijie.techtest.repository.MainRepository
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @Before
    fun setUp() {
        ApiClient.init("https://data.gov.sg/api/")
    }

    @Test
    fun getYearAmountData() {
        val viewModel = MainViewModel(MainRepository)
        val amountDataList = runBlocking(TestCoroutineDispatcher()) {
            MainRepository.getAmountData(44, 14)
        }
        val yearAmountDataList = viewModel.getYearAmountData(amountDataList.orEmpty())
        TestCase.assertEquals(11, yearAmountDataList.size)
    }

}