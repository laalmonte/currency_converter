package com.emapta.currencyconverter.Cases

import android.os.Parcel
import android.os.Parcelable

class CurrencyUseCase : Parcelable {
    var id: Long = 0
    var currency: String? = null
    var amount: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        id = `in`.readLong()
        currency = `in`.readString()
        amount = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(currency)
        dest.writeString(amount)
    }

    override fun toString(): String {
        return "{" +
                "id=" + id +
                ", currency='" + currency + '\''.toString() +
                ", amount='" + amount + '\''.toString() +
                '}'.toString()
    }


    companion object CREATOR : Parcelable.Creator<CurrencyUseCase> {
        override fun createFromParcel(parcel: Parcel): CurrencyUseCase {
            return CurrencyUseCase(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyUseCase?> {
            return arrayOfNulls(size)
        }
    }
}
