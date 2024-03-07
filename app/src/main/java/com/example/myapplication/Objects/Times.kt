package com.example.myapplication.Objects

import com.google.firebase.Timestamp

open class Times(var time: Timestamp) {



    companion object {
         var updatedLocal = ArrayList<Times>()
    }

}