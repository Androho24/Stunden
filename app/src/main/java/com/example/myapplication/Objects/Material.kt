package com.example.myapplication.Objects

class Material(var material: String,var unit:String) {

    open fun Material(){

    }

    companion object {
        var materials = ArrayList<Material>()
        var materialsLager = ArrayList<Material>()
    }
}