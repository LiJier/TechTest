package com.lijie.techtest.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lijie.techtest.R
import com.lijie.techtest.http.ApiClient
import com.lijie.techtest.http.onError
import com.lijie.techtest.http.onLoading
import com.lijie.techtest.http.onSuccess
import com.lijie.techtest.repository.IMainRepository
import com.lijie.techtest.repository.MainRepository
import com.lijie.techtest.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by lazy {
        viewModels<MainViewModel> {

            object : ViewModelProvider.Factory {

                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    try {
                        return modelClass.getConstructor(IMainRepository::class.java)
                            .newInstance(MainRepository)
                    } catch (e: InstantiationException) {
                        throw RuntimeException("Cannot create an instance of $modelClass", e)
                    } catch (e: IllegalAccessException) {
                        throw RuntimeException("Cannot create an instance of $modelClass", e)
                    }
                }

            }
        }.value
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.init("https://data.gov.sg/api/")
        mainViewModel.amountDataList.observe(this, { res ->
            res.onSuccess {
                Log.d("http", it.toString())
            }.onLoading {
                Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show()
            }.onError {
                Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
            }
        })
        mainViewModel.getAmountData()
    }

}