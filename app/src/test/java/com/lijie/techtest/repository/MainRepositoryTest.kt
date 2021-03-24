package com.lijie.techtest.repository

import com.lijie.techtest.http.ApiClient
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainRepositoryTest {

    @Before
    fun setUp() {
        ApiClient.init("https://data.gov.sg/api/")
    }

    @Test
    fun getAmountData() = runBlocking(TestCoroutineDispatcher()) {
        val amountData = MainRepository.getAmountData(44, 14)
        TestCase.assertEquals(44, amountData?.size)
    }
}