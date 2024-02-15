package com.example.myapplication

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
        tableMaterial!!.setLayoutManager(LinearLayoutManager(this));
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialMaterial)
        spinnerUnit = findViewById(R.id.spinnerAddMaterialMaterial)
        editMatName = findViewById(R.id.editTextNewMaterialMaterial)
        mainScrollView = findViewById(R.id.scrollMaterial)
        var adapter = MaterialAdapter(Material.materials,applicationContext)
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
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitList!!)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerUnit!!.adapter = dataAdapter
    }

    private fun onButtonClickListeners() {
        buttonAddMaterial!!.setOnClickListener {
            var mat = Material(editMatName!!.text.toString(),spinnerUnit!!.selectedItem!!.toString())


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

                    if(mat.material!!.contains(editTextFilter!!.text.toString(),ignoreCase = true)){
                        listMaterial.add(mat)
                    }



                }
                var adapter = MaterialAdapter(listMaterial,applicationContext)
                tableMaterial!!.adapter = adapter
            }
            else{
                var adapter = MaterialAdapter(Material.materials,applicationContext)
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