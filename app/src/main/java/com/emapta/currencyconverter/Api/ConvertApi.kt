package com.emapta.currencyconverter.Api

import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface ConvertApi {
    @Headers("Content-Type: application/json", "Accept: application/json")

    @GET("{amt}-{fromCurr}/{toCurr}/latest")
    fun convert(@Path("amt") amt: String, @Path("fromCurr") fromCurr: String, @Path("toCurr") toCurr: String): Observable<Response<ConvertResponse>>
}
