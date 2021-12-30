package com.mtr.kisanbazaar.models

import com.mtr.kisanbazaar.models.User

data class Post(
    var text:String="",
    var user: User = User(),
    var createdAt:Long=0L,
    val likedBy:ArrayList<String> =ArrayList()
)
