package com.androho.elektroeibauer.Material

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androho.elektroeibauer.Objects.CustomerMaterial
import com.androho.elektroeibauer.Objects.Material
import com.androho.elektroeibauer.R
import com.androho.elektroeibauer.XmlTool

class MaterialEditMain()  : AppCompatActivity() {


    var lastString =""
    var spinnerUnit : Spinner? = null
    var tableMaterial : RecyclerView? = null
    var editTextFilter : EditText? = null
    var buttonAddMaterial : Button? = null
    var buttonCancel : Button? = null
    var editMatName : EditText? = null
    var mainScrollView : ScrollView? = null
    var bundle : Bundle? = null

    var wasSaved : Boolean = false

    var oldMaterials = ArrayList<CustomerMaterial>()
    var name: String? = null
    var unit: String? = null
    var amount: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.material_edit_main_activity)
        tableMaterial = findViewById(R.id.recycleViewEditMaterial)
        editTextFilter = findViewById(R.id.editTextFilterEditMaterial)
        tableMaterial!!.layoutManager = LinearLayoutManager(this)
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialEditMaterial)
        spinnerUnit = findViewById(R.id.spinnerAddMaterialEditMaterial)
        editMatName = findViewById(R.id.editTextNewMaterialEditMaterial)
        mainScrollView = findViewById(R.id.scrollMaterial)
        buttonCancel = findViewById(R.id.buttonCancelEditMaterialMain)
        Material.connectMaterial()
        var adapter = MaterialAdapterEditMaterialEditMain(Material.connectedMaterials,applicationContext)
        tableMaterial!!.adapter = adapter
        bundle = Bundle()
        amount = intent.extras!!.getString("amount")
        unit = intent.extras!!.getString("unit")
        name = intent.extras!!.getString("name")



        onTextChanged()
        onButtonClickListeners()
        setSpinnerContent()

        editTextFilter!!.setText(name)

        oldMaterials = CustomerMaterial.customerMaterials
        var newMaterials = ArrayList<CustomerMaterial>()
        for (mat in CustomerMaterial.customerMaterials){
            if(mat.materialName == name && mat.materialAmount == amount && mat.materialUnit == unit){

            }
            else{
                newMaterials.add(mat)
            }
        }
        CustomerMaterial.customerMaterials = newMaterials


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
            builder.setMessage("Möchten sie folgendes Material hinzufügen?\n"+"Einheit: "+spinnerUnit!!.selectedItem.toString()+"\n"+"Name: "+editMatName!!.text.toString())
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
                var mat = Material(i+1,editMatName!!.text.toString(), spinnerUnit!!.selectedItem!!.toString(),"",0,false)
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

        buttonCancel!!.setOnClickListener {
            CustomerMaterial.customerMaterials = oldMaterials
            finish()
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
                var adapter = MaterialAdapterEditMaterialEditMain(listMaterial,applicationContext)
                tableMaterial!!.adapter = adapter
            }
            else{
                var adapter = MaterialAdapterEditMaterialEditMain(Material.ownMaterials,applicationContext)
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
            CustomerMaterial.customerMaterials = oldMaterials
        }
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    inner class MaterialAdapterEditMaterialEditMain(private var dataSet: ArrayList<Material>,private var context: Context) :
        RecyclerView.Adapter<MaterialAdapterEditMaterialEditMain.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView1: TextView
            val textView2: TextView
            val editText : EditText
            val button : Button
            var checkBox: CheckBox
            init {
                // Define click listener for the ViewHolder's View
                textView1 = view.findViewById(R.id.textView1)
                textView2 = view.findViewById(R.id.textView2)
                editText = view.findViewById(R.id.editTextHolder)
                button = view.findViewById(R.id.buttonHolder)
                checkBox = view.findViewById(R.id.checkBoxMaterialAdapterZugang)

            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.material_row_adapter_mat_act, viewGroup, false)

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
            viewHolder.checkBox.id = position
            viewHolder.button.id = position
            viewHolder.button.setOnClickListener {
                if(viewHolder.editText.text.toString()!= "") {
                    var newMaterial = CustomerMaterial()
                    newMaterial.materialName = viewHolder.textView2.text.toString()
                    newMaterial.materialUnit = viewHolder.textView1.text.toString()
                    if (viewHolder.checkBox.isChecked){
                        newMaterial.materialAmount = "+"+(Math.abs(viewHolder.editText.text.toString().toFloat())).toString()
                        newMaterial.materialZugang = true
                    }
                    else{
                        var amou = viewHolder.editText.text.toString().toFloat().toString()
                        newMaterial.materialZugang = false
                        newMaterial.materialAmount = amou
                    }
                    var sortedList =  CustomerMaterial.customerMaterials.sortedBy { s -> s.materialName }.toCollection(ArrayList<CustomerMaterial>())
                    CustomerMaterial.customerMaterials = sortedList
                    CustomerMaterial.customerMaterials.add(newMaterial)
                    Toast.makeText(context, "Material hinzugefügt", Toast.LENGTH_SHORT).show()
                    finish()

                }
            }

        }



        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size



    }
}