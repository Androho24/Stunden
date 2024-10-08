package com.androho.elektroeibauer.Objects

class Workers(s: String) {

    constructor() : this(s = "")
    var worker : String? = null

    companion object{
        var workerArray= ArrayList<Workers>()
    }
}