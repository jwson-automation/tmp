package com.project.tmp

import android.icu.text.SimpleDateFormat
import java.util.*

object Setting {
    val gameTypes =
        arrayOf("Game Type 1", "Game Type 2", "Game Type 3")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val LOCATION_PERMISSION_REQUEST_CODE = 123

    var latitude = 0.0
    var longitude = 0.0

}