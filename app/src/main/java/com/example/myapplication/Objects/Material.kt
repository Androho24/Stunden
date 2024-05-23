package com.example.myapplication.Objects

class Material(var id : Int, var material: String, var unit:String, var barcode:String?,var anzahl:Int,var bestellen:Boolean) {
    constructor() : this(id = 0,material ="",unit ="",barcode ="",0,false)


    companion object {

        fun connectMaterial(){
            var connectedMaterial = materials.clone() as ArrayList<Material>
            if (ownMaterials.isNotEmpty()){
                for (mat in ownMaterials){
                    connectedMaterial.add(mat)
                }
            }
            connectedMaterials = connectedMaterial
        }

        var connectedMaterials = ArrayList<Material>()
        var materials = ArrayList<Material>()
        var ownMaterials = ArrayList<Material>()
        var adminCustList = ArrayList<Material>()
    }
}