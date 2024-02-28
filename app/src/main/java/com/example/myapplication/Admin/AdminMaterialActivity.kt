package com.example.myapplication.Admin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.MaterialAdapterAddLager
import com.example.myapplication.Lager.LagerActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.Material.EditMaterialList
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.Material
import com.google.android.material.navigation.NavigationView
import com.example.myapplication.R
import com.example.myapplication.XmlTool

class AdminMaterialActivity : AppCompatActivity() {

    var lastString = ""
    var navView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    var tableMaterial: RecyclerView? = null
    var editTextFilter: EditText? = null
    var buttonAddMaterial: Button? = null
    var spinnerUnit: Spinner? = null
    var editMatName: EditText? = null
    var mainScrollView: ScrollView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_material_activity)
        navView = findViewById(R.id.nav_view_admin)
        tableMaterial = findViewById(R.id.recycleViewAdminMaterialMain)
        editTextFilter = findViewById(R.id.editTextFilterAdminMaterialMain)
        buttonAddMaterial = findViewById(R.id.buttonAddAdminMaterialMain)
        spinnerUnit = findViewById(R.id.spinnerAddAdminMaterialMain)
        editMatName = findViewById(R.id.editTextFilterAdminMaterialMain)
        mainScrollView = findViewById(R.id.scrollViewAdminMaterialMain)
        Material.connectMaterial()
        var adapter = MaterialAdapterMainAdmin(Material.connectedMaterials,applicationContext)
        tableMaterial!!.adapter = adapter
        drawerLayout = findViewById(R.id.drawer_layout_admin)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

        navView = findViewById(R.id.nav_view_admin)
        /**/
        navView!!.bringToFront()

        navView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemLager -> {
                    val myIntent = Intent(this, LagerActivity::class.java)
                    startActivity(myIntent)
                    true
                }

                R.id.regieBericht -> {
                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                    true
                }

                else -> {
                    drawerLayout!!.closeDrawers()
                    true
                }
            }
        }

        onTextChanged()
        onButtonClickListeners()
        setSpinnerContent()


    }

    private fun onButtonClickListeners() {
        buttonAddMaterial!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material hinzufügen")
            builder.setMessage("Möchten sie folegendes Material hinzufügen?\n\n" + "Einheit: " + spinnerUnit!!.selectedItem.toString() + "\n" + "Name: " + editMatName!!.text.toString())


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                var materialExists = false
                for (mat in Material.connectedMaterials) {
                    if (editMatName!!.text.toString() == mat.material) {
                        materialExists = true
                    }
                }

                if (materialExists) {
                    Toast.makeText(this, "Material existiert bereits", Toast.LENGTH_SHORT).show()
                } else {
                    var mat = Material(
                        editMatName!!.text.toString(),
                        spinnerUnit!!.selectedItem!!.toString(),
                        ""
                    )
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

    private fun setSpinnerContent() {
        var unitList = ArrayList<String>()
        unitList.add("Stck")
        unitList.add("m")

        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitList)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerUnit!!.adapter = dataAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onTextChanged() {

        editTextFilter!!.doAfterTextChanged {

            if (lastString.length < editTextFilter!!.text.toString().length) {
                var i = 0
                var listMaterial = ArrayList<Material>()
                for (mat in Material.connectedMaterials) {

                    if (mat.material.contains(
                            editTextFilter!!.text.toString(),
                            ignoreCase = true
                        )
                    ) {
                        listMaterial.add(mat)
                    }


                }
                var adapter = MaterialAdapterMainAdmin(listMaterial, applicationContext)
                tableMaterial!!.adapter = adapter
            } else {
                Material.connectMaterial()
                var adapter = MaterialAdapterMainAdmin(Material.connectedMaterials,applicationContext)
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
        if (requestCode == LagerActivity.adminEditMaterialRequest){
            Material.connectMaterial()
            var adapter = MaterialAdapterMainAdmin(Material.connectedMaterials,applicationContext)
            tableMaterial!!.adapter = adapter
        }
    }

    inner class MaterialAdapterMainAdmin(
        private var dataSet: ArrayList<Material>,
        private var context: Context
    ) :
        RecyclerView.Adapter<MaterialAdapterMainAdmin.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView1: TextView
            val textView2: TextView
            val editText: EditText
            val button: Button

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

            viewHolder.button.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    var activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, AdminMaterialEditActivity::class.java)
                    intent.putExtra("unit", viewHolder.textView1.text.toString())
                    intent.putExtra("name", viewHolder.textView2.text.toString())
                    activity.startActivityForResult(intent, LagerActivity.adminEditMaterialRequest)

                }
            }
            )
            viewHolder.textView1.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    var activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, AdminMaterialEditActivity::class.java)
                    intent.putExtra("unit", viewHolder.textView1.text.toString())
                    intent.putExtra("name", viewHolder.textView2.text.toString())
                    activity.startActivityForResult(intent, LagerActivity.adminEditMaterialRequest)

                }

            })
            viewHolder.textView2.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    var activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, AdminMaterialEditActivity::class.java)
                    intent.putExtra("unit", viewHolder.textView1.text.toString())
                    intent.putExtra("name", viewHolder.textView2.text.toString())
                    activity.startActivityForResult(intent, LagerActivity.adminEditMaterialRequest)


                }
            })

        }


        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }

}