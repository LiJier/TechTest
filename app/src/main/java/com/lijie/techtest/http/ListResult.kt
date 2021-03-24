package com.lijie.techtest.http

class ListResult<T> : IHttpResult<List<T>> {

    private var help: String? = null
    private var success: Boolean? = null
    private var result: Result<T>? = null

    override fun isSuccess(): Boolean {
        return success == true
    }

    override fun getResultData(): List<T>? {
        return result?.records
    }

    override fun getMessage(): String? {
        return help
    }

}

data class Result<T>(
    var records: List<T>? = null
)

