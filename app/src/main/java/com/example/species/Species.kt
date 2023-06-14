package com.example.species

import android.os.Parcel
import android.os.Parcelable

data class Species(
    val name : String,
    val family : String,
    val species : String,
    val description : String,
    val img_url : String,
    val characteristic : String,
    val rating : String,
    val kingdom :String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(family)
        parcel.writeString(species)
        parcel.writeString(description)
        parcel.writeString(img_url)
        parcel.writeString(characteristic)
        parcel.writeString(rating)
        parcel.writeString(kingdom)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Species> {
        override fun createFromParcel(parcel: Parcel): Species {
            return Species(parcel)
        }

        override fun newArray(size: Int): Array<Species?> {
            return arrayOfNulls(size)
        }
    }
}
