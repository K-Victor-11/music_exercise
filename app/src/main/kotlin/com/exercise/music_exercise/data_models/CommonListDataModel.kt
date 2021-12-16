package com.exercise.music_exercise.data_models

import java.io.Serializable

data class CommonListDataModel(
    val view_type:Int,
    val view_item:Any?
): Serializable
