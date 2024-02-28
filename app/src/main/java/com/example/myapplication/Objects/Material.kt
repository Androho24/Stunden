package com.example.myapplication.Objects

class Material(var material: String,var unit:String,var barcode:String) {

    open fun Material(){

    }

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
    }
}