package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.MaterialAdapter
import com.example.myapplication.Lager.LagerActivity
import com.example.myapplication.Objects.Material


class AddMaterialActivity : AppCompatActivity() {
    var lastString =""
    var spinnerUnit : Spinner? = null
    var tableMaterial : RecyclerView? = null
    var editTextFilter : EditText? = null
    var buttonAddMaterial : Button? = null
    var editMatName : EditText? = null
    var mainScrollView :ScrollView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_material_activity)
        tableMaterial = findViewById(R.id.recycleViewMaterial)
        editTextFilter = findViewById(R.id.editTextFilterMaterial)
        tableMaterial!!.layoutManager = LinearLayoutManager(this)
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialMaterial)
        spinnerUnit = findViewById(R.id.spinnerAddMaterialMaterial)
        editMatName = findViewById(R.id.editTextNewMaterialMaterial)
        mainScrollView = findViewById(R.id.scrollMaterial)
        Material.connectMaterial()
        var adapter = MaterialAdapter(Material.connectedMaterials,applicationContext)
        tableMaterial!!.adapter = adapter


        onTextChanged()
        onButtonClickListeners()
        setSpinnerContent()

    }

    private fun setSpinnerContent() {
        var unitList = ArrayList<String>()
        unitList.add("Stck")
        unitList.add("m")

        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitList)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerUnit!!.adapter = dataAdapter
    }

    private fun onButtonClickListeners() {
        buttonAddMaterial!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material hinzufügen")
            builder.setMessage("Möchten sie folegendes Material hinzufügen?\n\n"+"Einheit: "+spinnerUnit!!.selectedItem.toString()+"\n"+"Name: "+editMatName!!.text.toString())


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                var materialExists = false
                for (mat in Material.connectedMaterials){
                    if (editMatName!!.text.toString() == mat.material){
                        materialExists = true
                    }
                }

                if (materialExists){
                    Toast.makeText(this,"Material existiert bereits", Toast.LENGTH_SHORT).show()
                }
                else {
                    var mat = Material(editMatName!!.text.toString(), spinnerUnit!!.selectedItem!!.toString(),"")
                    Material.ownMaterials.add(mat)
                    editTextFilter!!.setText(mat.material)
                    var xmlTool = XmlTool()
                    xmlTool.saveOwnMaterialsToXml(Material.ownMaterials, applicationContext)
                    mainScrollView!!.fullScroll(ScrollView.FOCUS_UP)
                }
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }
    }

    private fun onTextChanged() {

        editTextFilter!!.doAfterTextChanged {

            if (lastString.length < editTextFilter!!.text.toString().length) {
                var i = 0
                var listMaterial = ArrayList<Material>()
                for (mat in Material.connectedMaterials) {

                    if(mat.material.contains(editTextFilter!!.text.toString(),ignoreCase = true)){
                        listMaterial.add(mat)
                    }



                }
                var adapter = MaterialAdapter(listMaterial,applicationContext)
                tableMaterial!!.adapter = adapter
            }
            else{
                var adapter = MaterialAdapter(Material.connectedMaterials,applicationContext)
                tableMaterial!!.adapter = adapter
            }

        }



        editMatName!!.setOnClickListener {
            mainScrollView!!.fullScroll(ScrollView.FOCUS_DOWN)
        }

        editTextFilter!!.setOnClickListener {
            mainScrollView!!.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LagerActivity.materialEditList){
            var adapter = MaterialAdapter(Material.connectedMaterials,applicationContext)
            tableMaterial!!.adapter = adapter
            editTextFilter!!.setText("")
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}