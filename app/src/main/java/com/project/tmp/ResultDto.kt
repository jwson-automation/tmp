package com.project.tmp

import java.io.Serializable
import java.util.*

class ResultDto : Serializable {
    var nickname: String
    var description: String
    var latitude: Int
    var longitude: Int
    val date: Date // Use the Date data type
    val gameType: String

    constructor(nickname: String, description: String, latitude: Int, longitude: Int, date: Date, gameType:String) {
        this.nickname = nickname
        this.description = description
        this.latitude = latitude
        this.longitude = longitude
        this.date = date
        this.gameType = gameType
    }

    override fun toString(): String {
        return "ResultDto(nickname='$nickname', description='$description', latitude='$latitude', longitude='$longitude', date='$date', gameType='$gameType')"
    }
}