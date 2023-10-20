package com.example.myapplication.Objects

import java.util.UUID

class Customer(var customerId : String, var name : String,var preName: String,var streetName: String,var streetNumber:String, var plz:String,var location:String, var projectNumber: String) {

    open fun Customer(){

    }

    companion object{
        var arrayCustomers = ArrayList<Customer>()

    }
}