package com.lijie.techtest.http

/**
 * Create by LiJie at 2021年03月24日
 */

/**
 * 获取接口返回数据data
 */
fun <T> IHttpResult<T>.handleData(dataCanBeNull: Boolean = false): T? {
    if (this.isSuccess()) {
        val t = this.getResultData()
        return if (dataCanBeNull) {
            t
        } else {
            t ?: throw HttpResultException("result data is null")
        }
    } else {
        throw HttpResultException(this.getMessage())
    }
}

class HttpResultException : Exception {

    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )

}