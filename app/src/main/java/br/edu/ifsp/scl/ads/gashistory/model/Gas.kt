package br.edu.ifsp.scl.ads.gashistory.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Gas(
    val value: Double = 0.00,
    val date: Date = Date(1900, 1, 1)
) : Parcelable