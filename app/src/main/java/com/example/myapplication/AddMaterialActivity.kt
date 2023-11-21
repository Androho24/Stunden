package com.example.myapplication

import android.content.Intent
import android.media.Image
import android.opengl.Visibility
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.Material


class AddMaterialActivity : AppCompatActivity() {
    var lastString =""
    var tableMaterial : RecyclerView? = null
    var editTextFilter : EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_material_activity)
        tableMaterial = findViewById(R.id.recycleViewMaterial)
        editTextFilter = findViewById(R.id.editTextFilterMaterial)

        var buttonId = 0

        for (material in Material.materials){
            var row = TableRow(this.applicationContext)
            row.minimumHeight = 150

            var materialView = TextView(applicationContext)
            materialView.setText(material.material)
            materialView.id = buttonId
            row.addView(materialView)

            var unitView = TextView(applicationContext)
            unitView.setText(material.unit)
            unitView.id = buttonId
            row.addView(unitView)

            var text = EditText(applicationContext)
            text.id = buttonId
            text.width = 100
            text.inputType = InputType.TYPE_CLASS_NUMBER
            row.addView(text)

            var button = ImageView(this)
            button.id= buttonId
            button.setImageResource(R.drawable.accept)
            row.addView(button)
            button.setOnClickListener{
                var newMaterial = CustomerMaterial()
                newMaterial.materialName = materialView.findViewById<TextView>(buttonId).text.toString()
                newMaterial.materialUnit = unitView.findViewById<TextView>(buttonId).text.toString()
                newMaterial.materialAmount = text.findViewById<TextView>(buttonId).text.toString()

                CustomerMaterial.customerMaterials.add(newMaterial)

                Toast.makeText(applicationContext,"Material hinzugefügt",Toast.LENGTH_SHORT).show()



            }


            tableMaterial!!.addView(row)
        buttonId++
        }


        onTextChanged()


    }

    private fun onTextChanged() {

        editTextFilter!!.doAfterTextChanged {

            if (lastString.length < editTextFilter!!.text.toString().length) {
                var i = 0
                for (row in tableMaterial!!.children.iterator()) {
                    //   var rowe = tableMaterial!!.getChildAt(i) as TableRow
                    var childTable = tableMaterial!!.getChildAt(i)
                    if (childTable is TableRow) {

                        for (x in 0 until childTable.getChildCount()) {
                            if (x == 0) {

                                var view = childTable.getChildAt(0) as TextView
                                if (view.text.contains(editTextFilter!!.text.toString(), ignoreCase = true)) {
                                    lastString = editTextFilter!!.text.toString()
                                } else {
                                    childTable.removeAllViews()
                                }

                            }
                        }

                    }



                    i++
                }
            }
            else{
var buttonId = 0
                tableMaterial!!.removeAllViews()

                for (material in Material.materials){
                    if (material.material.contains(editTextFilter!!.text.toString(), ignoreCase = true)){
                    var row = TableRow(this.applicationContext)
                        row.minimumHeight = 150
                    var materialView = TextView(applicationContext)
                    materialView.setText(material.material)
                    row.addView(materialView)

                    var unitView = TextView(applicationContext)
                    unitView.setText(material.unit)
                    row.addView(unitView)

                        var text = EditText(applicationContext)
                        text.width = 100
                        text.inputType = InputType.TYPE_CLASS_NUMBER
                        row.addView(text)

                        var button = ImageView(this)
                        button.id= buttonId
                        button.setImageResource(R.drawable.accept)
                        row.addView(button)
                        buttonId++
                        button.setOnClickListener{
                            var newMaterial = CustomerMaterial()
                            newMaterial.materialName = materialView.text.toString()
                            newMaterial.materialUnit = unitView.text.toString()
                            newMaterial.materialAmount = text.text.toString()

                            CustomerMaterial.customerMaterials.add(newMaterial)

                            Toast.makeText(applicationContext,"Material hinzugefügt",Toast.LENGTH_SHORT).show()

                        }


                    tableMaterial!!.addView(row)
                    }
                }
            }

        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}