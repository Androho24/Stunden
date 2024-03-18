package com.example.myapplication.Objects

class CustomerMaterial {
    var materialName:String? = null
    var materialUnit :String? = null
    var materialAmount:String? = null
    var materialZugang:Boolean? = null

    companion object{
        var customerMaterials = ArrayList<CustomerMaterial>()
        var customerMaterialsLager = ArrayList<CustomerMaterial>()
    }
}