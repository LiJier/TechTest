package com.lijie.techtest.repository

import com.lijie.techtest.entity.AmountData
import com.lijie.techtest.http.Api
import com.lijie.techtest.http.ApiClient
import com.lijie.techtest.http.handleData

/**
 * 数据获取接口
 */
interface IMainRepository {

    /**
     * 获取使用数据
     */
    suspend fun getAmountData(
        limit: Int,
        offset: Int,
    ): List<AmountData>?

}

object MainRepository : IMainRepository {

    /**
     * 获取数据实现
     */
    override suspend fun getAmountData(
        limit: Int,
        offset: Int,
    ): List<AmountData>? {
        return ApiClient.api<Api>().getAmountData(limit = limit, offset = offset).handleData(false)
    }


}