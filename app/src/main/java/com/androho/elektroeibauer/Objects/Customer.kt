package com.androho.elektroeibauer.Objects

open class Customer(var customerId : String, var name : String, var preName: String, var streetName: String, var streetNumber:String, var plz:String, var location:String, var projectNumber: String,var customerExpanded : CustomerExpanded) {

    open fun Customer(){

    }

    companion object{
        var arrayCustomers = ArrayList<Customer>()

    }
}