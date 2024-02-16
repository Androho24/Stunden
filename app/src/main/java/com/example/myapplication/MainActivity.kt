package com.example.myapplication


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfRenderer
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.MaterialAdapterMain
import com.example.myapplication.Adapter.WorktimeAdapterMain
import com.example.myapplication.Interfaces.MainActivityMatInterface
import com.example.myapplication.Interfaces.MainActivityWorktimeInterface
import com.example.myapplication.Lager.LagerActivity
import com.example.myapplication.Objects.Customer
import com.example.myapplication.Objects.CustomerExpanded
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.Workers
import com.example.myapplication.Objects.WorktimeMain
import com.example.myapplication.Pdf.PDFCreator
import com.example.myapplication.Pdf.PreviewPdfActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import com.itextpdf.layout.Document
import org.apache.commons.io.FileUtils
import java.io.File
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@SuppressLint("SetJavaScriptEnabled")
class MainActivity : AppCompatActivity(), WorkTimeFragment.onWorktimeEventLisnter,
    CustomerFragment.onNewCustomerEventListener,CustomerClientFragment.onClientEventListener,
    MainActivityMatInterface,MainActivityWorktimeInterface {
    // var ourWorkbook: Workbook? = null
    //var sheet: Sheet? = null
    var buttonSetDate: Button? = null
    var buttonPreview: Button? = null
    var buttonAddMaterial : Button? = null
    var date: TextView? = null
    var buttonClearAll: Button? = null
    var buttonEditCustomer: Button? = null
    var buttonAddWorkTime: Button? = null
    var buttonChangeLocation: Button? = null
    var tableWorkTimes: RecyclerView? = null
    var tableMaterial: RecyclerView? = null
    var spinnerCustomer: Spinner? = null
    var workDescriptionInput: TextInputLayout? = null
    var editTextChangeLocation : EditText? = null
    var xmlTool: XmlTool? = null
    var scrollViewMateriel:NestedScrollView? = null
    var mContext: Context? = null
    var mHtmlString: String? = null
    var mPdfFile: File? = null
    var mPdfPrintAttrs: PrintAttributes? = null
    var mIsCurrentlyConverting: Boolean? = null
    var webview: WebView? = null
    var doc: Document? = null
    var myIcon: Drawable? = null
    var pdfCreator: PDFCreator? = null
    var fusedLocationClient : LocationManager? = null
    var isNightModeOn : Boolean = false
//    var locationByGPS
//    var locationByNetwork
    var location = "Moosthenning"
    var tableTextColor = Color.BLACK

    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var navView : NavigationView? = null
    var menuDraw : Menu? = null


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSetDate = findViewById<Button>(R.id.buttonAddDate)
        buttonPreview = findViewById(R.id.buttonPreview)
        date = findViewById<TextView>(R.id.textViewDate)
        buttonClearAll = findViewById<Button>(R.id.buttonClearMain)
        buttonEditCustomer = findViewById<Button>(R.id.buttonEditCustomerMain)
        buttonAddWorkTime = findViewById(R.id.buttonAddWorkTimeMain)
        buttonAddMaterial= findViewById(R.id.buttonAddMaterialMain)
        buttonChangeLocation = findViewById(R.id.buttonChangeLocationMain)
        editTextChangeLocation = findViewById(R.id.editTextChangeLocationMain)
        tableWorkTimes = findViewById(R.id.tableWorktimes)
        tableWorkTimes!!.layoutManager = LinearLayoutManager(this)
        tableMaterial = findViewById(R.id.tableMaterialMain)
        tableMaterial!!.layoutManager = LinearLayoutManager(this)

        scrollViewMateriel = findViewById(R.id.scrollView4)

        spinnerCustomer = findViewById(R.id.spinnerCustomerMain)
        workDescriptionInput = findViewById(R.id.textInputWorkDescription)
        date!!.text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
        fusedLocationClient = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

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

        val filedir : File = File("/storage/emulated/0/Documents/ElektroEibauer/")
        if (!filedir.exists()) {
            var fdir = File("/storage/emulated/0/Documents/", "ElektroEibauer")
            fdir.mkdir()
        }

        val filedirMat : File = File("/storage/emulated/0/Documents/ElektroEibauer/Materialschein/")
        if (!filedirMat.exists()) {
            var fdir = File("/storage/emulated/0/Documents/ElektroEibauer", "Materialschein")
            fdir.mkdir()
        }


        navView = findViewById(R.id.nav_view)
/**/
        navView!!.bringToFront()

        navView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemLager -> {
                    val myIntent = Intent(this, LagerActivity::class.java)
                    startActivity(myIntent)
                    true
                }
                else -> {
                    Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

        when (mode) {
            Configuration.UI_MODE_NIGHT_NO -> {isNightModeOn = false}
            Configuration.UI_MODE_NIGHT_YES -> {isNightModeOn = true}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {isNightModeOn = false }
        }
        if (isNightModeOn){
            tableTextColor = Color.WHITE
        }
        else{
            tableTextColor = Color.BLACK
        }


        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.

            }
            else -> {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 111)

            }
        }

        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.

            }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),112)
            }
        }




        var locationByGPS = fusedLocationClient!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        var locationByNetwork = fusedLocationClient!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        locationByNetwork?.let {
            locationByNetwork = locationByGPS
        }
        var latitude : Double = 0.0
        var longitude : Double = 0.0
        var locationCoordinates : Location? = null
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


        var geocoder : Geocoder
        var addresses: List<Address>
        geocoder =  Geocoder(this, Locale.getDefault())

        if(geocoder != null) {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)!!
            if (!addresses.isEmpty()) {
                location = addresses[0].locality
            }
        }
        else{
            location = "Moosthenning"
        }
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        )
        var asdf: PdfRenderer
        var xmlTool = XmlTool()
        var input = File("/storage/emulated/0/documents/ElektroEibauer/Arbeitsnachweis.xlsx")
        var output = File("/storage/emulated/0/documents/ElektroEibauer/Arbeitsnachweiss.pdf")
        pdfCreator = PDFCreator()
        myIcon = resources.getDrawable(R.drawable.img)


Workers.workerArray.add("Matthias Höpfler")

        Workers.workerArray.add("Heizer Oliver")


        Workers.workerArray.add("Franz Eibauer")
        Workers.workerArray.add("Alexander Geisperger")
        Workers.workerArray.add("Tägliche Pausenzeit")

        if (Environment.isExternalStorageManager()) {

        } else {
            //request for the permission
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        val res = R.drawable.img
        val file = ImageView(applicationContext)
        file.setImageResource(res)



        setScrollViews()
        loadXml()
        setSpinnerCustomer()
        buttonOnClickListeners()
        editTextOnClickListeners()


    }

    private fun setScrollViews() {
        scrollViewMateriel!!.requestFocus()
    }


    private fun loadXml() {
        xmlTool = XmlTool()
        xmlTool!!.loadSavedProfilefromXml(applicationContext)
        xmlTool!!.loadMaterialsFromXml(applicationContext)
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

    private fun editTextOnClickListeners() {
        date!!.setOnClickListener {

        }
    }


    fun buttonOnClickListeners() {
        buttonSetDate!!.setOnClickListener {

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
                    date!!.text =
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

        buttonClearAll!!.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eingaben löschen")
            builder.setMessage("Wollen Sie wirklich alle Eingaben löschen?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                WorktimeMain.staticWorkTimeArrayList.clear()
                workDescriptionInput!!.editText!!.setText("")
                var adapterWT = WorktimeAdapterMain(WorktimeMain.staticWorkTimeArrayList,applicationContext,this)
                tableWorkTimes!!.adapter = adapterWT
                CustomerMaterial.customerMaterials.clear()
                var adapterMaterial = MaterialAdapterMain(CustomerMaterial.customerMaterials,applicationContext,this)
                tableMaterial!!.adapter = adapterMaterial
                var textColor = Color.BLACK
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()




        }

        buttonPreview!!.setOnClickListener {
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
                File("/storage/emulated/0/Documents/ElektroEibauer/" + selectedCustomer.name + "_" + selectedCustomer.preName)
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
            var path = "/storage/emulated/0/Documents/ElektroEibauer/test.pdf"
            val folder = "/storage/emulated/0/Documents/ElektroEibauer/"
            val f = File(folder, selectedCustomer.name + "_" + selectedCustomer.preName)
            f.mkdir()
            if(WorktimeMain.staticWorkTimeArrayList.isEmpty()){
                Toast.makeText(this,"Bitte Arbeitszeit hinzufügen",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var pathToSave =
                "/storage/emulated/0/Documents/ElektroEibauer/" + selectedCustomer.name + "_" + selectedCustomer.preName + "/" + "Arbeitsnachweis_Nr_" + count + "_" + date!!.text.toString() + "_" + selectedCustomer.name + "_" + customerCount + "_" + WorktimeMain.staticWorkTimeArrayList.iterator()
                    .next().workerName + ".pdf"
            var document = pdfCreator!!.returnCreatedDocument(
                myIcon,
                selectedCustomer,
                date!!.text.toString(),
                workDescriptionInput!!.editText!!.text.toString(),
                WorktimeMain.staticWorkTimeArrayList,
                path,
                count,
                location
            )
            document.close()

            val myIntent = Intent(this, PreviewPdfActivity::class.java)
            //Optional parameters
            myIntent.putExtra("path", path)
            myIntent.putExtra("pathToSave", pathToSave)
            myIntent.putExtra("customerPrename", selectedCustomer.preName)
            myIntent.putExtra("customerName", selectedCustomer.name)

            startActivity(myIntent)

        }

        buttonAddMaterial!!.setOnClickListener {
            val myIntent = Intent(this,AddMaterialActivity::class.java)
            startActivityForResult(myIntent, materialResult)
        }


        buttonEditCustomer!!.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager

            val customerFragmentDialog: CustomerFragment =
                CustomerFragment.newInstance("Some Title")

            customerFragmentDialog.show(fm, "fragment_edit_name")
        }

        buttonAddWorkTime!!.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager
            val editNameDialogFragment: WorkTimeFragment =
                WorkTimeFragment.newInstance("Some Title")

            editNameDialogFragment.show(fm, "fragment_edit_name")
        }

        buttonChangeLocation!!.setOnClickListener {
            location = editTextChangeLocation!!.text.toString()
        }


    }


    private fun selectPdfFromStorage() {
        Toast.makeText(this, "selectPDF", Toast.LENGTH_LONG).show()
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(browseStorage, "Select PDF"), PDF_SELECTION_CODE
        )
    }


    /* private fun createCell(sheetRow: Row, columnIndex: Int, cellValue: String?) {
         //create a cell at a passed in index
         val ourCell = sheetRow.getCell(columnIndex)
         //add the value to it
         //a cell can be empty. That's why its nullable
         ourCell?.setCellValue(cellValue)
     }*/


    private fun setWorkDescription() {
        // val row = sheet!!.getRow(5)
        //var cell = row.getCell(8)
        //  cell.setCellValue(workDescriptionInput!!.editText!!.text.toString())
    }

    /*  private fun setExcelWorktimes(staticWorkTimeArrayList: ArrayList<WorktimeMain>) {
          var i = 5
          for (worktime in staticWorkTimeArrayList) {
              val row = sheet!!.getRow(i)
              var cell = row.getCell(0)
              cell!!.setCellValue(date!!.text.toString())
              worktime.date = date!!.text.toString()
              cell = row.getCell(1)
              cell.setCellValue(worktime.beginWorktime)
              cell = row.getCell(2)
              cell.setCellValue(worktime.endWorktime)
              cell = row.getCell(3)
              cell.setCellValue(worktime.workTime)
              var t = 0
              cell = row.getCell(4)
              cell.setCellValue(worktime.wegeRuest)
              cell = row.getCell(5)
              cell.setCellValue("k.A.")
              cell = row.getCell(6)
              cell.setCellValue(worktime.workerName)
              i++
          }

      }

      private fun setExcelCustomer(customer: Customer) {
          val row = sheet!!.getRow(2)
          var cell = row.getCell(0)
          cell!!.cellType = CellType.STRING
          cell!!.setCellValue(customer.name+"\n"+customer.preName+"\n"+customer.streetName+" "+customer.streetNumber+"\n"+customer.plz+" "+customer.location )
          cell = row.getCell(7)
          cell!!.setCellValue(customer.name+"\n"+customer.preName+"\n"+customer.streetName+" "+customer.streetNumber+"\n"+customer.plz+" "+customer.location )
      }*/

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == customerResult) {
            setSpinnerCustomer()

        }

        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPdfFromStorage = data.data
            showPdfFromUri(selectedPdfFromStorage)
        }

        if (requestCode == materialResult){
           // tableMaterial!!.removeAllViews()
            var adapter = MaterialAdapterMain(CustomerMaterial.customerMaterials,applicationContext,this)
            tableMaterial!!.adapter = adapter




        }


    }

    private fun showPdfFromUri(uri: Uri?) {
        /* pdfView.fromUri(uri)
             .defaultPage(0)
             .spacing(10)
             .load()*/
    }






    companion object {
        var customerResult = 100
        var materialResult = 101
        private const val PDF_SELECTION_CODE = 99

    }



    override fun worktimeListner(
        beginWorktime: String,
        endWorkTime: String,
        wegeRuest: String,
        workers: ArrayList<String>
    ) {
        var i = 0
        for (worker in workers) {
            var newWorktime = WorktimeMain()
            newWorktime.beginWorktime = beginWorktime
            newWorktime.endWorktime = endWorkTime
            var stringBeginWork = beginWorktime.split(":")
            var stringEndWork = endWorkTime.split(":")

            var hours = Integer.parseInt(stringEndWork[0]) - Integer.parseInt(stringBeginWork[0])
            var minutes = Integer.parseInt(stringEndWork[1]) - Integer.parseInt(stringBeginWork[1])

            if (minutes < 0) {
                hours = hours - 1
                minutes = 60 + minutes
            }

            var minutesFloat = minutes.toFloat() / 60
            var formatMinute =
                minutesFloat.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
                    .split(("."))
            var formatedMinute = formatMinute[1]

            newWorktime.workTime = hours.toString() + "," + formatedMinute

            newWorktime.wegeRuest = wegeRuest
            newWorktime.workerName = workers.get(i)
            WorktimeMain.staticWorkTimeArrayList.add(newWorktime)
            i++
        }
        var adapter = WorktimeAdapterMain(WorktimeMain.staticWorkTimeArrayList,applicationContext,this)
        tableWorkTimes!!.adapter = adapter



    }


    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }*/

    override fun onNewCustomerListener() {
        xmlTool!!.saveProfilesToXml(Customer.arrayCustomers, applicationContext)
        setSpinnerCustomer()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
    override fun onClientEventlistener(customerID: String) {
      var asdf = customerID
    }


    override fun onMaterialDeletedListener() {
        var adapter = MaterialAdapterMain(CustomerMaterial.customerMaterials,applicationContext,this)
        tableMaterial!!.adapter = adapter
    }

    override fun onWorktimeDeletedListener() {
        var adapter = WorktimeAdapterMain(WorktimeMain.staticWorkTimeArrayList,applicationContext,this)
        tableWorkTimes!!.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            111 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                   Toast.makeText(applicationContext,"Um die App zu verwenden bitte Standortfreigabe aktiviere",Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}


