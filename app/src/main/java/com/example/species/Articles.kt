package com.example.species

import android.os.Parcel
import android.os.Parcelable

data class Articles(
    val author: String,
    val date:String,
    val description: String,
    val title: String,
    val img_url: String,
    val author_img: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    constructor() : this (
        "",
        "",
        "",
        "",
        "",
        ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(date)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeString(img_url)
        parcel.writeString(author_img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Articles> {
        override fun createFromParcel(parcel: Parcel): Articles {
            return Articles(parcel)
        }

        override fun newArray(size: Int): Array<Articles?> {
            return arrayOfNulls(size)
        }
    }
}
