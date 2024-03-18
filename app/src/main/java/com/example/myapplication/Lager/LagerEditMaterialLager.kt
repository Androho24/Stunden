package com.example.myapplication.Lager

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.Material
import com.example.myapplication.R
import com.example.myapplication.XmlTool

class LagerEditMaterialLager : AppCompatActivity() {
    var lastString =""
    var tableMaterial : RecyclerView? = null
    var editTextFilter : EditText? = null
    var buttonAddMaterial : Button? = null
    var editUnit : Spinner? = null
    var editMatName : EditText? = null
    var mainScrollView : ScrollView? = null

    var wasSaved : Boolean = false

    var oldMaterials = ArrayList<CustomerMaterial>()
    var name: String? = null
    var unit: String? = null
    var amount: String? = null
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
        Material.connectMaterial()
        var adapter = MaterialAdapterEditLager(Material.connectedMaterials,applicationContext)
        tableMaterial!!.adapter = adapter


        onTextChanged()
        onButtonClickListeners()
        setSpinnerContent()

        amount = intent.extras!!.getString("amount")
        unit = intent.extras!!.getString("unit")
        name = intent.extras!!.getString("name")

        editTextFilter!!.setText(name)

        oldMaterials = CustomerMaterial.customerMaterialsLager
        var newMaterials = ArrayList<CustomerMaterial>()
        for (mat in CustomerMaterial.customerMaterialsLager){
            if(mat.materialName == name && mat.materialAmount == amount && mat.materialUnit == unit){

            }
            else{
                newMaterials.add(mat)
            }
        }
        CustomerMaterial.customerMaterialsLager = newMaterials





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
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material hinzufügen")
            builder.setMessage("Möchten sie folgendes Material hinzufügen?\n"+"Einheit: "+editUnit!!.selectedItem.toString()+"\n"+"Name: "+editMatName!!.text.toString())
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                var materialExists = false
                for (mat in Material.connectedMaterials){
                    if (editMatName!!.text.toString() == mat.material){
                        materialExists = true
                    }
                }

                if (materialExists){
                    Toast.makeText(this,"Material existiert bereits",Toast.LENGTH_SHORT).show()
                }
                else {
                    var i =0
                    for (mat in Material.ownMaterials){
                        if (mat.id > 0){
                            i = mat.id
                        }
                    }
                    var mat = Material(i+1,editMatName!!.text.toString(), editUnit!!.selectedItem!!.toString(),"",0,false)
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
                var adapter = MaterialAdapterEditLager(listMaterial,applicationContext)
                tableMaterial!!.adapter = adapter
            }
            else{
                var adapter = MaterialAdapterEditLager(Material.connectedMaterials,applicationContext)
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
        super.onBackPressed()
        if (wasSaved == false){
            CustomerMaterial.customerMaterialsLager = oldMaterials
        }
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LagerActivity.materialEditList){
            Material.connectMaterial()
            var adapter = MaterialAdapterEditLager(Material.ownMaterials,applicationContext)
            editTextFilter!!.setText("")
        }
    }

    inner class MaterialAdapterEditLager (private var dataSet: ArrayList<Material>,private var context: Context) :
        RecyclerView.Adapter<MaterialAdapterEditLager.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView1: TextView
            val textView2: TextView
            val editText : EditText
            val button : Button
            init {
                // Define click listener for the ViewHolder's View
                textView1 = view.findViewById(R.id.textViewUnitAddLagerAdapter)
                textView2 = view.findViewById(R.id.textViewNameAddLagerAdapter)
                editText = view.findViewById(R.id.editTextAmountAddLagerAdapter)
                button = view.findViewById(R.id.buttonAddAddLagerAdapter)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.material_add_lager_adapter, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val m = dataSet.get(position)
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.textView1.text = m.unit
            viewHolder.textView1.id = position
            viewHolder.textView2.text = m.material
            viewHolder.textView2.id = position
            viewHolder.editText.id = position
            viewHolder.button.id = position
            viewHolder.button.setOnClickListener {
                if(viewHolder.editText.text.toString()!= "") {
                    var newMaterial = CustomerMaterial()
                    newMaterial.materialName = viewHolder.textView2.text.toString()
                    newMaterial.materialUnit = viewHolder.textView1.text.toString()
                    newMaterial.materialAmount = viewHolder.editText.text.toString().toFloat().toString()
                    CustomerMaterial.customerMaterialsLager.add(newMaterial)
                    wasSaved = true
                    Toast.makeText(context, "Material hinzugefügt", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }



        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size



    }
}