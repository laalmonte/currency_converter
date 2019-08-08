package com.emapta.currencyconverter.Api

import com.google.gson.annotations.SerializedName

class ConvertResponse {
    @SerializedName("amount")
    var amount: String? = null

    @SerializedName("currency")
    var currency: String? = null

    override fun toString(): String {
        return "{" +
                "amount='" + amount + '\''.toString() +
                ", currency='" + currency + '\''.toString() +
                '}'.toString()
    }
}
