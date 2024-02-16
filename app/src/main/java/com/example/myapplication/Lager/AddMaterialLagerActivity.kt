package com.example.myapplication.Lager

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.MaterialAdapter
import com.example.myapplication.Adapter.MaterialAdapterAddLager
import com.example.myapplication.Objects.Material
import com.example.myapplication.R
import com.example.myapplication.XmlTool

class AddMaterialLagerActivity : AppCompatActivity() {
    var lastString =""
    var tableMaterial : RecyclerView? = null
    var editTextFilter : EditText? = null
    var buttonAddMaterial : Button? = null
    var editUnit : Spinner? = null
    var editMatName : EditText? = null
    var mainScrollView : ScrollView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.material_lager_activity)
        tableMaterial = findViewById(R.id.recycleViewMaterialLagerAddLager)
        editTextFilter = findViewById(R.id.editTextFilterMaterialAddLager)
        tableMaterial!!.layoutManager = LinearLayoutManager(this)
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialMaterialAddLager)
        editUnit = findViewById(R.id.spinnerLagerAddLager)
        editMatName = findViewById(R.id.editTextNewMaterialMaterialAddLager)
        mainScrollView = findViewById(R.id.scrollMaterialLager)
        var adapter = MaterialAdapterAddLager(Material.materials,applicationContext)
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
        editUnit!!.adapter = dataAdapter
    }

    private fun onButtonClickListeners() {
        buttonAddMaterial!!.setOnClickListener {
            var mat = Material(editMatName!!.text.toString(),editUnit!!.toString())
            Material.materials.add(mat)
            editTextFilter!!.setText(mat.material)
            var xmlTool = XmlTool()
            xmlTool.saveMaterialsToXml(Material.materials,applicationContext)
            mainScrollView!!.fullScroll(ScrollView.FOCUS_UP)

        }

    }

    private fun onTextChanged() {

        editTextFilter!!.doAfterTextChanged {

            if (lastString.length < editTextFilter!!.text.toString().length) {
                var i = 0
                var listMaterial = ArrayList<Material>()
                for (mat in Material.materials) {

                    if(mat.material.contains(editTextFilter!!.text.toString(),ignoreCase = true)){
                        listMaterial.add(mat)
                    }



                }
                var adapter = MaterialAdapterAddLager(listMaterial,applicationContext)
                tableMaterial!!.adapter = adapter
            }
            else{
                var adapter = MaterialAdapterAddLager(Material.materials,applicationContext)
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

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}