package com.lijie.techtest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lijie.techtest.databinding.ActivityMainBinding
import com.lijie.techtest.http.ApiClient
import com.lijie.techtest.http.onError
import com.lijie.techtest.http.onLoading
import com.lijie.techtest.http.onSuccess
import com.lijie.techtest.repository.IMainRepository
import com.lijie.techtest.repository.MainRepository
import com.lijie.techtest.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        AmountAdapter(arrayListOf())
    }
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.refreshLayout.setEnableLoadMore(false)
        viewBinding.amountRecyclerView.layoutManager = LinearLayoutManager(this)
        //初始化设置一个空的adapter，以便加载失败时可以使用下拉刷新
        viewBinding.amountRecyclerView.adapter = adapter
        ApiClient.init("https://data.gov.sg/api/")
        mainViewModel.amountDataListLiveData.observe(this, { res ->
            res.onSuccess {
                viewBinding.refreshLayout.finishRefresh()
                val yearAmountDataList = mainViewModel.getYearAmountData(it.orEmpty())
                adapter.amountDataList = yearAmountDataList
                adapter.notifyDataSetChanged()
            }.onLoading {
            }.onError {
                viewBinding.refreshLayout.finishRefresh()
                Toast.makeText(this, "发生异常${it?.message},使用本地缓存", Toast.LENGTH_SHORT).show()
                val yearAmountDataList = mainViewModel.getYearAmountData(mainViewModel.getCache())
                adapter.amountDataList = yearAmountDataList
                adapter.notifyDataSetChanged()
            }
        })
        viewBinding.refreshLayout.setOnRefreshListener {
            mainViewModel.getAmountData()
        }
        viewBinding.refreshLayout.autoRefresh()
    }

}