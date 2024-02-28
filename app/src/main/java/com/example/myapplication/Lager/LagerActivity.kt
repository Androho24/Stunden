package com.example.myapplication.Lager

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.MaterialAdapterLager
import com.example.myapplication.CustomerClientFragment
import com.example.myapplication.CustomerFragment
import com.example.myapplication.Interfaces.LagerActivityInterface
import com.example.myapplication.MainActivity
import com.example.myapplication.Objects.Customer
import com.example.myapplication.Objects.CustomerExpanded
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.Workers
import com.example.myapplication.Pdf.LagerPdfCreator
import com.example.myapplication.Pdf.PreviewPdfActivity
import com.example.myapplication.R
import com.example.myapplication.XmlTool
import com.google.android.material.navigation.NavigationView
import org.apache.commons.io.FileUtils
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LagerActivity : AppCompatActivity(),LagerActivityInterface,CustomerClientFragment.onClientEventListener,CustomerFragment.onNewCustomerEventListener {

    var drawerLayout : DrawerLayout? = null
    var navView : NavigationView? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var editTextDate : TextView? = null

    var spinnerCustomer : Spinner? = null
    var tableMaterial : RecyclerView? = null
    var buttonAddMaterial: Button? = null
    var buttonPreview: Button? = null
    var buttonClearAll : Button? = null
    var buttonEditDate : Button? =null
    var buttonEditCustomer: Button? = null


    var checkBoxZugang : CheckBox? = null
    var checkBoxAbgang : CheckBox? = null

    var spinnerWorkers : Spinner? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lager)

        drawerLayout = findViewById(R.id.drawer_layout_lager)
        spinnerCustomer= findViewById(R.id.spinnerCustomerLager)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialLager)
        tableMaterial = findViewById(R.id.tableMaterialLager)
        tableMaterial!!.layoutManager = LinearLayoutManager(this)
        editTextDate = findViewById(R.id.textViewDatumLager)
        buttonPreview = findViewById(R.id.buttonPreviewLager)
        buttonClearAll = findViewById(R.id.buttonLagerClearAll)
        buttonEditDate = findViewById(R.id.buttonLagerDate)
        buttonEditCustomer = findViewById(R.id.buttonEditCustomerLager)

        checkBoxAbgang = findViewById(R.id.checkBoxLagerAbgang)
        checkBoxZugang = findViewById(R.id.checkBoxLagerZugang)

        spinnerWorkers = findViewById(R.id.spinnerWorkersLager)

        editTextDate!!.text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

        // to make the Navigation drawer icon always appear on the action bar

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        var adapter = MaterialAdapterLager(CustomerMaterial.customerMaterialsLager,this)
        tableMaterial!!.adapter = adapter



        navView = findViewById(R.id.nav_view_lager)


        navView!!.bringToFront()

        navView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
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

        setButtonOnClickListeners()
        setSpinnerWorkers()
        setSpinnerCustomer()
        setCheckboxOnClickListeners()

    }

    private fun setSpinnerCustomer() {
        var customerList = ArrayList<String>()
        for (customer in Customer.arrayCustomers) {
            customerList.add(customer.name + ", " + customer.preName + ", " + customer.streetName)
        }
        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, customerList)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerCustomer!!.adapter = dataAdapter
    }

    private fun setCheckboxOnClickListeners() {
       checkBoxZugang!!.setOnCheckedChangeListener { buttonView, isChecked ->
           if (isChecked){
               checkBoxAbgang!!.isChecked = false
           }
       }
        checkBoxAbgang!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                checkBoxZugang!!.isChecked = false
            }
        }
    }

    private fun setButtonOnClickListeners() {
        buttonEditDate!!.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our text view.
                    editTextDate!!.text =
                        dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year.toString()
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }
        buttonAddMaterial!!.setOnClickListener{
            var intent = Intent(this,AddMaterialLagerActivity::class.java)
            startActivityForResult(intent, materialresult)
        }

        buttonEditCustomer!!.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager

            val customerFragmentDialog: CustomerFragment =
                CustomerFragment.newInstance("Some Title")

            customerFragmentDialog.show(fm, "fragment_edit_name")
        }

        buttonClearAll!!.setOnClickListener {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eingaben löschen")
                builder.setMessage("Wollen Sie wirklich alle Eingaben löschen?")


                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                 CustomerMaterial.customerMaterialsLager.clear()
                    var adapterMaterial = MaterialAdapterLager(CustomerMaterial.customerMaterialsLager,this)
                    tableMaterial!!.adapter = adapterMaterial
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->

                }

                builder.show()
        }
        buttonAddMaterial!!.setOnClickListener {
            val intent = Intent(this,AddMaterialLagerActivity::class.java)
            startActivityForResult(intent, materialresult)
        }

        buttonPreview!!.setOnClickListener {
            var richtungMat = ""
            if (checkBoxAbgang!!.isChecked && !checkBoxZugang!!.isChecked){
                richtungMat = "Abgang"
            }
            else if (checkBoxZugang!!.isChecked && !checkBoxAbgang!!.isChecked){
                richtungMat = "Zugang"
            }
        else if(!checkBoxZugang!!.isChecked && !checkBoxAbgang!!.isChecked ){
            Toast.makeText(applicationContext,"Bitte Zugang oder Abgang auswählen",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
        }

            var customExpand = CustomerExpanded("","","","","","")
            var selectedCustomer = Customer("", "", "", "", "", "", "", "",customExpand)
            var spinnerStringCustomer = spinnerCustomer!!.selectedItem.toString()

            var name = spinnerStringCustomer.split(", ")
            if (name.lastIndex == 2) {
                for (customer in Customer.arrayCustomers) {
                    if (customer.name == name[0] && customer.preName == name[1] && customer.streetName == name[2]) {
                        selectedCustomer = customer
                    }
                }
            }
            else if (name.lastIndex == 1){
                for (customer in Customer.arrayCustomers){
                    if (customer.name == name[0] && customer.preName == name[1]){
                        selectedCustomer = customer
                    }
                }
                for (customer in Customer.arrayCustomers){
                    if (customer.name == name[0] && customer.streetName == name[1]){
                        selectedCustomer = customer
                    }
                }
            }
            else if (name.lastIndex == 0){
                for(customer in Customer.arrayCustomers){
                    if (customer.name == name[0]){
                        selectedCustomer = customer
                    }
                }
            }

            val fi: File =
                File("/storage/emulated/0/Documents/ElektroEibauer/Materialschein/" + selectedCustomer.name + "_" + selectedCustomer.preName)
            val files = fi.listFiles()
            var count = 1
            if (files != null) for (i in files.indices) {
                count++
                val file = files[i]
                if (file.isDirectory) {
                    FileUtils.getFile(file.absolutePath)
                }
            }

            var customerCount = 1
            var path = "/storage/emulated/0/Documents/ElektroEibauer/Materialschein/test.pdf"
            val folder = "/storage/emulated/0/Documents/ElektroEibauer/Materialschein/"
            val f = File(folder, selectedCustomer.name + "_" + selectedCustomer.preName)
            f.mkdir()
            var myIcon = resources.getDrawable(R.drawable.img)
            var pdfCreator = LagerPdfCreator()
            var pathToSave =
                "/storage/emulated/0/Documents/ElektroEibauer/Materialschein/" + selectedCustomer.name + "_" + selectedCustomer.preName + "/" + "Materialschein_Nr_" + count + "_" + editTextDate!!.text.toString() + "_" + selectedCustomer.name + "_" + customerCount + "_" + ".pdf"
            var document = pdfCreator.returnLagerPdf(
                myIcon,
                editTextDate!!.text.toString(),
                spinnerWorkers!!.selectedItem.toString(),
                richtungMat,
                path,
                selectedCustomer,
                "Moosthenning"
            )
            document.close()

            val myIntent = Intent(this, PreviewPdfActivity::class.java)
            //Optional parameters
            myIntent.putExtra("path", path)
            myIntent.putExtra("pathToSave", pathToSave)
            myIntent.putExtra("customerPrename", selectedCustomer.preName)
            myIntent.putExtra("customerName", selectedCustomer.name)
            myIntent.putExtra("isLager","true")

            startActivity(myIntent)
        }
    }

    private fun setSpinnerWorkers() {


        var workerList = ArrayList<String>()
        for (worker in Workers.workerArray){
            workerList.add(worker.toString())
        }
        var dataAdapterWorker = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,workerList)
        dataAdapterWorker.setDropDownViewResource(R.layout.spinner_style)
        spinnerWorkers!!.adapter = dataAdapterWorker
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == materialresult){
        var adapter = MaterialAdapterLager(CustomerMaterial.customerMaterialsLager,this)
            tableMaterial!!.adapter = adapter
        }
        if (requestCode == customerResult) {
            setSpinnerCustomer()

        }
    }

    companion object{
        var materialresult = 103
        var customerResult = 100
        var materialEditResult = 1005
        var materialEditList = 1006
    }

    override fun onDeletedListener() {
       var adapter = MaterialAdapterLager(CustomerMaterial.customerMaterialsLager,this)
        tableMaterial!!.adapter = adapter
    }

    override fun onClientEventlistener(customerID: String) {

    }

    override fun onNewCustomerListener() {
        var xmlTool = XmlTool()
        xmlTool.saveProfilesToXml(Customer.arrayCustomers, applicationContext)
        setSpinnerCustomer()
    }
}