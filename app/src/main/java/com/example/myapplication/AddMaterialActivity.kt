package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.CustomAdapter
import com.example.myapplication.Objects.Material


class AddMaterialActivity : AppCompatActivity() {
    var lastString =""
    var tableMaterial : RecyclerView? = null
    var editTextFilter : EditText? = null
    var buttonAddMaterial : Button? = null
    var editUnit : EditText? = null
    var editMatName : EditText? = null
    var mainScrollView :ScrollView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_material_activity)
        tableMaterial = findViewById(R.id.recycleViewMaterial)
        editTextFilter = findViewById(R.id.editTextFilterMaterial)
        tableMaterial!!.setLayoutManager(LinearLayoutManager(this));
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialMaterial)
        editUnit = findViewById(R.id.editTextUnitMaterial)
        editMatName = findViewById(R.id.editTextNewMaterialMaterial)
        mainScrollView = findViewById(R.id.scrollMaterial)
        var adapter = CustomAdapter(Material.materials)
        tableMaterial!!.adapter = adapter


        onTextChanged()
        onButtonClickListeners()

    }

    private fun onButtonClickListeners() {
        buttonAddMaterial!!.setOnClickListener {
            var mat = Material(editMatName!!.text.toString(),editUnit!!.text.toString())


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
                var adapter = CustomAdapter(listMaterial)
                tableMaterial!!.adapter = adapter
            }
            else{
                var adapter = CustomAdapter(Material.materials)
                tableMaterial!!.adapter = adapter
            }

        }

        editUnit!!.setOnClickListener {
            mainScrollView!!.fullScroll(ScrollView.FOCUS_DOWN)
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