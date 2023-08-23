package com.project.tmp

import android.icu.text.SimpleDateFormat
import java.util.*

object Setting {
    val gameTypes =
        arrayOf("오버워치", "리그오브레전드", "마인크래프트")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val LOCATION_PERMISSION_REQUEST_CODE = 123

    var latitude = 0.0
    var longitude = 0.0

}