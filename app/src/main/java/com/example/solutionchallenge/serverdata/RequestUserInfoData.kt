package com.example.solutionchallenge.serverdata

data class RequestUserInfoData(
    val nickname : String,
    val gender : String,
    val physicalAbilityLevel : String,
    var core : Boolean,
    var rightUpperArm : Boolean,
    var rightLowerArm : Boolean,
    var leftUpperArm : Boolean,
    var leftLowerArm : Boolean,
    var rightUpperLeg : Boolean,
    var rightLowerLeg : Boolean,
    var leftUpperLeg : Boolean,
    var leftLowerLeg : Boolean,
)
