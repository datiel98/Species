package com.example.species

import android.os.Parcel
import android.os.Parcelable

data class SearchSpecies(val searchSpeciesName: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(searchSpeciesName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchSpecies> {
        override fun createFromParcel(parcel: Parcel): SearchSpecies {
            return SearchSpecies(parcel)
        }

        override fun newArray(size: Int): Array<SearchSpecies?> {
            return arrayOfNulls(size)
        }
    }

}
