package com.project.tmp

import java.io.Serializable

class ResultDto : Serializable {
    var nickname: String
    var description: String
    var latitude: Int
    var longitude: Int

    constructor(nickname: String, description: String, latitude: Int, longitude: Int) {
        this.nickname = nickname
        this.description = description
        this.latitude = latitude
        this.longitude = longitude
    }

    override fun toString(): String {
        return "ResultDto(nickname='$nickname', description='$description', latitude='$latitude', longitude='$longitude')"
    }
}