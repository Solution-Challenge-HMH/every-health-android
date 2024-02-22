package com.example.solutionchallenge.serverdata

import com.google.gson.annotations.SerializedName

data class RequestUserLoginData(
    @SerializedName("accessToken")
    val accessToken : String
)
