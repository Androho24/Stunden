package com.androho.elektroeibauer.Lager

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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
import com.androho.elektroeibauer.Admin.AdminMaterialActivity
import com.androho.elektroeibauer.Admin.WorkerActivity.AdminWorkerActivity
import com.androho.elektroeibauer.CustomerClientFragment
import com.androho.elektroeibauer.CustomerFragment
import com.androho.elektroeibauer.Interfaces.LagerActivityInterface
import com.androho.elektroeibauer.MainActivity
import com.androho.elektroeibauer.Objects.Customer
import com.androho.elektroeibauer.Objects.CustomerExpanded
import com.androho.elektroeibauer.Objects.CustomerMaterial
import com.androho.elektroeibauer.Objects.Workers
import com.androho.elektroeibauer.Pdf.LagerPdfCreator
import com.androho.elektroeibauer.Pdf.PreviewPdfActivity
import com.androho.elektroeibauer.R
import com.androho.elektroeibauer.Static.StaticClass
import com.androho.elektroeibauer.XmlTool
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class LagerActivity : AppCompatActivity(),LagerActivityInterface,CustomerClientFragment.onClientEventListener,CustomerFragment.onNewCustomerEventListener {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    var location : String = "Moosthenning"
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

    var editTextChangeLocation : EditText? = null
    var buttonChangeLocation: Button? = null

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
        editTextChangeLocation = findViewById(R.id.editTextChangeLocationLagerActivity)
        buttonChangeLocation = findViewById(R.id.buttonChangeLocationLagerActivity)

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

      /* for (i in 0..26) {

            var customerMat = CustomerMaterial()
            customerMat.materialAmount = "1"
            customerMat.materialName = "test"
            customerMat.materialUnit = "Stck"
        CustomerMaterial.customerMaterialsLager.add(customerMat)
        }*/


        navView = findViewById(R.id.nav_view_lager)


        navView!!.bringToFront()

        navView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.regieBericht -> {
               StaticClass.isSelectedFromNavView = true
                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                    true
                }
                R.id.itemAdminMaterial ->{
                    StaticClass.isSelectedFromNavView = true
                    val myIntent = Intent(this,AdminMaterialActivity::class.java)
                    startActivity(myIntent)
                    true
                }
                R.id.itemAdminWorker ->{
                    StaticClass.isSelectedFromNavView = true
                    val myIntent = Intent(this, AdminWorkerActivity::class.java)
                    startActivity(myIntent)
                    true
                }
                R.id.logout ->{
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("776731154059-67mhfidrlet3uvohblnb51ee2qhgq0at.apps.googleusercontent.com")
                        .requestEmail()
                        .build()
                    mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                    mGoogleSignInClient.signOut()
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
        getLocationLager()

    }

    @SuppressLint("MissingPermission")
    private fun getLocationLager() {
        var fusedLocationClient = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var locationByGPS = fusedLocationClient!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        var locationByNetwork =
            fusedLocationClient!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        locationByNetwork?.let {
            locationByNetwork = locationByGPS
        }
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var locationCoordinates: Location? = null
        if (locationByGPS != null && locationByNetwork != null) {
            if (locationByGPS.accuracy > locationByNetwork!!.accuracy) {
                locationCoordinates = locationByGPS
                latitude = locationCoordinates.latitude
                longitude = locationCoordinates.longitude
                // use latitude and longitude as per your need
            } else {
                locationCoordinates = locationByNetwork
                latitude = locationCoordinates!!.latitude
                longitude = locationCoordinates.longitude
                // use latitude and longitude as per your need
            }
        }


        var geocoder: Geocoder
        var locale: Locale = Locale("de", "de", "Moosthenning")
        var address: Address = Address(locale)

        var addresses: List<Address> = emptyList()

        geocoder = Geocoder(this, Locale.getDefault())

        if (geocoder != null) {
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1)!!
                if (!addresses.isEmpty()) {
                    location = addresses[0].locality
                } else {
                    location = "Moosthenning"
                }
            } catch (e: IOException) {

            }
        } else {
            location = "Moosthenning"
        }

        editTextChangeLocation!!.setText(location)
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

        buttonChangeLocation!!.setOnClickListener {
            location = editTextChangeLocation!!.text.toString()
        }
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


        buttonPreview!!.setOnClickListener {
            if (checkBoxAbgang!!.isChecked){
                for (custMat in CustomerMaterial.customerMaterialsLager){
                    custMat.materialAmount = (Math.abs(custMat.materialAmount!!.toFloat())*-1).toString()

                }
            }

            if(checkBoxZugang!!.isChecked){
                for (custMat1 in CustomerMaterial.customerMaterialsLager){
                    custMat1.materialAmount = (Math.abs(custMat1.materialAmount!!.toFloat())).toString()
                }

            }

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
                location
            )
            document.close()

            val myIntent = Intent(this, PreviewPdfActivity::class.java)
            //Optional parameters
            myIntent.putExtra("path", path)
            myIntent.putExtra("pathToSave", pathToSave)
            myIntent.putExtra("customerPrename", selectedCustomer.preName)
            myIntent.putExtra("customerName", selectedCustomer.name)
            myIntent.putExtra("isLager","true")

            startActivityForResult(myIntent,MainActivity.previewResult)
        }
    }

    private fun setSpinnerWorkers() {


        var workerList = ArrayList<String>()
        for (worker in Workers.workerArray){
            workerList.add(worker.worker.toString())
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


        if(requestCode == MainActivity.previewResult){
            for (mat in CustomerMaterial.customerMaterialsLager){
                mat.materialAmount = Math.abs(mat.materialAmount.toString().toFloat()).toString()
            }
        }
        if(requestCode == materialresult){
            var newMats = ArrayList<CustomerMaterial>()
            for (mat in CustomerMaterial.customerMaterialsLager) {
                var matExists = false
                var amount: Float = 0f
                if (!newMats.isEmpty()) {
                    for (mat2 in newMats) {
                        if (mat.materialName == mat2.materialName) {
                            matExists = true
                            amount = mat2.materialAmount!!.toFloat()
                            mat2.materialAmount =
                                (mat.materialAmount!!.toFloat() + mat2.materialAmount!!.toFloat()).toString()
                        }
                    }
                }
                if (matExists == false) {
                    newMats.add(mat)
                }

            }
            CustomerMaterial.customerMaterialsLager = newMats
            var sortedList =  CustomerMaterial.customerMaterialsLager.sortedBy { s -> s.materialName }.toCollection(ArrayList<CustomerMaterial>())
            CustomerMaterial.customerMaterialsLager = sortedList


        var adapter = MaterialAdapterLager(CustomerMaterial.customerMaterialsLager,this)
            tableMaterial!!.adapter = adapter
        }
        if (requestCode == customerResult) {
            setSpinnerCustomer()

        }
        if (requestCode == materialEditResult){
            var adapter = MaterialAdapterLager(CustomerMaterial.customerMaterialsLager,this)
            tableMaterial!!.adapter = adapter
        }
    }

    companion object{

        var isSelectedFromNavView = false
        var materialresult = 103
        var customerResult = 100
        var materialEditResult = 1005
        var materialEditList = 1006
        var adminEditMaterialRequest = 1007

    }

    override fun onDeletedListener() {
       var adapter = MaterialAdapterLager(CustomerMaterial.customerMaterialsLager,this)
        tableMaterial!!.adapter = adapter
    }

    override fun onClientEventlistener(customerID: String) {

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }




    override fun onNewCustomerListener() {
        var xmlTool = XmlTool()
        xmlTool.saveProfilesToXml(Customer.arrayCustomers, applicationContext)
        setSpinnerCustomer()
    }

    inner class MaterialAdapterLager(private var dataSet: ArrayList<CustomerMaterial>, onDeletedListener:LagerActivityInterface) :
        RecyclerView.Adapter<MaterialAdapterLager.ViewHolder>() {

        private var onDeletedClickListener = onDeletedListener

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewAmount: TextView
            val textViewUnit: TextView
            val textViewName : TextView
            val buttonDelete : Button
            init {
                // Define click listener for the ViewHolder's View
                textViewAmount = view.findViewById(R.id.textViewLagerMatAmountMain)
                textViewUnit = view.findViewById(R.id.textViewLagerMatUnitMain)
                textViewName = view.findViewById(R.id.textViewLagerMaterialNameMain)
                buttonDelete = view.findViewById(R.id.buttonLagerDeleteMatMain)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.material_lager_adapter, viewGroup, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val m = dataSet.get(position)
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.textViewAmount.id = position
            viewHolder.textViewAmount.text = m.materialAmount
            viewHolder.textViewAmount.setOnClickListener ( object: View.OnClickListener{
                override fun onClick(v: View?) {
                    val activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, LagerEditMaterialLager::class.java)
                    intent.putExtra("amount",viewHolder.textViewAmount.text.toString())
                    intent.putExtra("unit",viewHolder.textViewUnit.text.toString())
                    intent.putExtra("name",viewHolder.textViewName.text.toString())
                    activity.startActivityForResult(intent,LagerActivity.materialEditResult)
                }

            })
            viewHolder.textViewUnit.id = position
            viewHolder.textViewUnit.text = m.materialUnit
            viewHolder.textViewUnit.setOnClickListener ( object: View.OnClickListener{
                override fun onClick(v: View?) {
                    val activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, LagerEditMaterialLager::class.java)
                    intent.putExtra("amount",viewHolder.textViewAmount.text.toString())
                    intent.putExtra("unit",viewHolder.textViewUnit.text.toString())
                    intent.putExtra("name",viewHolder.textViewName.text.toString())
                    activity.startActivityForResult(intent,LagerActivity.materialEditResult)
                }

            })
            viewHolder.textViewName.id = position
            viewHolder.textViewName.text = m.materialName
            viewHolder.textViewName.setOnClickListener ( object: View.OnClickListener{
                override fun onClick(v: View?) {
                    val activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, LagerEditMaterialLager::class.java)
                    intent.putExtra("amount",viewHolder.textViewAmount.text.toString())
                    intent.putExtra("unit",viewHolder.textViewUnit.text.toString())
                    intent.putExtra("name",viewHolder.textViewName.text.toString())
                    activity.startActivityForResult(intent,LagerActivity.materialEditResult)
                }

            })
            viewHolder.buttonDelete.id = position
            viewHolder.buttonDelete.setOnClickListener {
                val builder = android.app.AlertDialog.Builder(this@LagerActivity)
                builder.setTitle("Material hinzufügen")
                builder.setMessage("Möchten Sie folgendes Material löschen?\n\n Name: " + viewHolder.textViewName!!.text.toString())


                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                var newMats = ArrayList<CustomerMaterial>()
                for (mat in CustomerMaterial.customerMaterialsLager){
                    if (viewHolder.textViewName.text == mat.materialName){

                    }
                    else {
                        newMats.add(mat)
                    }
                }
                CustomerMaterial.customerMaterialsLager = newMats
                onDeletedClickListener.onDeletedListener()
                }


                builder.setNegativeButton(android.R.string.no) { dialog, which ->

                }

                builder.show()


            }
        }

        // Replace the contents of a view (invoked by the layout manager)




        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size



    }
}