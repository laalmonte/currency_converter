package com.emapta.currencyconverter.Api

import com.google.gson.annotations.SerializedName

class DataWrapper<T> {

    @SerializedName("data")
    var data: T? = null

    constructor() {}

    constructor(data: T) {
        this.data = data
    }

    override fun toString(): String {
        return "{" +
                "mData=" + data +
                '}'.toString()
    }
}
