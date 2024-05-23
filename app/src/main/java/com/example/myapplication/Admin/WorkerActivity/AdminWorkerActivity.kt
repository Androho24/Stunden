package com.example.myapplication.Admin.WorkerActivity

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.Admin.AdminMaterialActivity
import com.example.myapplication.Database.GoogleFirebase
import com.example.myapplication.Interfaces.FirestoreTimeCallback
import com.example.myapplication.Interfaces.FirestoreWokersFromDbCallback
import com.example.myapplication.Lager.LagerActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.Times
import com.example.myapplication.Objects.Workers
import com.example.myapplication.R
import com.example.myapplication.Static.StaticClass
import com.example.myapplication.XmlTool
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp

class AdminWorkerActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var buttonSave : Button
    lateinit var buttonCancel : Button
    lateinit var buttonDelete : Button
    lateinit var buttonCreateWorker : Button
    lateinit var spinnerWorkers : Spinner
    lateinit var editTextWorker : EditText
    lateinit var editTextNewWorker : EditText

    var navView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    var alertView : View? = null
    var context: Context? = null
    var loadingImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_worker_activity)
        drawerLayout = findViewById(R.id.drawer_layout_admin_worker)
        buttonSave = findViewById(R.id.buttonSaveAdminWorkerAct)
        buttonCancel = findViewById(R.id.buttonCancelAdminWorkerAct)
        buttonDelete = findViewById(R.id.buttonDeleteAdminWorkerAct)
        buttonCreateWorker = findViewById(R.id.buttonCreateWorkerAdminWorkerAct)
        editTextWorker = findViewById(R.id.editTextAdminWorkerNameAct)
        editTextNewWorker = findViewById(R.id.editTextNewWorkerAdminWorkerAct)
        navView = findViewById(R.id.nav_view_admin_worker)
        spinnerWorkers = findViewById(R.id.spinnerAdminWorkersAct)
        GoogleFirebase.createAuthConnection()
        setLoadingImage()

        val alertDialog = AlertDialog.Builder(this@AdminWorkerActivity)
        alertDialog.setTitle("Login")

        val inflater = this.layoutInflater
        alertView = inflater.inflate(R.layout.password_dialog, null)
        var email = alertView!!.findViewById(R.id.usernameLoginDialog) as EditText
        var password = alertView!!.findViewById(R.id.passwordLoginDialog) as EditText



        alertDialog.setView(alertView)
        alertDialog.setPositiveButton("Login",null)



            .setNeutralButton(
                "Passwort zurücksetzen",
                DialogInterface.OnClickListener { dialog, which ->

                    GoogleFirebase.auth.sendPasswordResetEmail(email.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(ContentValues.TAG, "Email sent.")
                            }
                        }

                    val alertDialog = AlertDialog.Builder(this@AdminWorkerActivity)
                    alertDialog.setTitle("Login")

                    val inflater = this.layoutInflater
                    var alertView = inflater.inflate(R.layout.password_dialog, null)
                    var email = alertView.findViewById(R.id.usernameLoginDialog) as EditText
                    var password = alertView.findViewById(R.id.passwordLoginDialog) as EditText
                    alertDialog.setView(alertView)
                        .setNeutralButton(
                            "Passwort zurücksetzen",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (!email.text.toString().equals("")) {
                                    GoogleFirebase.auth.sendPasswordResetEmail(email.text.toString())
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    this,
                                                    "Email gesendet",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            })
                        .setPositiveButton("Login",
                            DialogInterface.OnClickListener { dialog, which ->


                                GoogleFirebase.auth.signInWithEmailAndPassword(
                                    email.text.toString(),
                                    password.text.toString()
                                )
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                                            val user = GoogleFirebase.auth.currentUser
                                            GoogleFirebase.createDBConnectionAndLoadMaterialUpdatedAt(
                                                object : FirestoreTimeCallback {
                                                    override fun onCallback() {
                                                        super.onCallback()
                                                        startUp()
                                                        loadXml()
                                                    }

                                                    override fun onFailureCallback() {
                                                        super.onFailureCallback()
                                                        Toast.makeText(
                                                            applicationContext,
                                                            "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                })









                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(
                                                ContentValues.TAG,
                                                "signInWithEmail:failure",
                                                task.exception
                                            )

                                            Toast.makeText(
                                                applicationContext,
                                                "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            alertDialog.show()

                                        }
                                    }
                            })

                    alertDialog.show()


                })
            .setPositiveButton("Login",
                DialogInterface.OnClickListener { dialog, which ->


                    GoogleFirebase.auth.signInWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(ContentValues.TAG, "signInWithEmail:success")
                                val user = GoogleFirebase.auth.currentUser
                                GoogleFirebase.createDBConnectionAndLoadMaterialUpdatedAt(object :
                                    FirestoreTimeCallback {
                                    override fun onCallback() {
                                        super.onCallback()
                                        startUp()
                                        loadXml()
                                    }

                                    override fun onFailureCallback() {
                                        super.onFailureCallback()
                                        Toast.makeText(
                                            applicationContext,
                                            "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                            Toast.LENGTH_SHORT
                                        )
                                    }
                                })






                                actionBarDrawerToggle =
                                    ActionBarDrawerToggle(
                                        this,
                                        drawerLayout,
                                        R.string.nav_open,
                                        R.string.nav_close
                                    )

                                // pass the Open and Close toggle for the drawer layout listener
                                // to toggle the button
                                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                                supportActionBar!!.setDisplayShowTitleEnabled(false)
                                // pass the Open and Close toggle for the drawer layout listener
                                // to toggle the button
                                drawerLayout!!.addDrawerListener(actionBarDrawerToggle!!)
                                actionBarDrawerToggle!!.syncState()

                                navView = findViewById(R.id.nav_view_admin_worker)

                                navView!!.bringToFront()
                                context = applicationContext

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
                                        R.id.itemAdminMaterial ->{
                                            StaticClass.isSelectedFromNavView = true
                                            val myIntent = Intent(this,AdminMaterialActivity::class.java)
                                            startActivity(myIntent)
                                            true
                                        }
                                        R.id.logout ->{
                                            val gso = GoogleSignInOptions.Builder(
                                                GoogleSignInOptions.DEFAULT_SIGN_IN)
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

                                onButtonClickListeners()


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    applicationContext,
                                    "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val alertDialog = AlertDialog.Builder(this@AdminWorkerActivity)
                                alertDialog.setTitle("Login")

                                /* val inflater = this.layoutInflater
                                 var alertView = inflater.inflate(R.layout.password_dialog, null)
                                 var email =
                                     alertView.findViewById(R.id.usernameLoginDialog) as EditText
                                 var password =
                                     alertView.findViewById(R.id.passwordLoginDialog) as EditText*/
                                alertDialog.setView(alertView)
                                    .setNeutralButton(
                                        "Passwort zurücksetzen",
                                        DialogInterface.OnClickListener { dialog, which ->

                                            GoogleFirebase.auth.sendPasswordResetEmail(email.text.toString())
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Toast.makeText(
                                                            this,
                                                            "Email gesendet",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }

                                            val alertDialog =
                                                AlertDialog.Builder(this@AdminWorkerActivity)
                                            alertDialog.setTitle("Login")

                                            val inflater = this.layoutInflater
                                            var alertView =
                                                inflater.inflate(R.layout.password_dialog, null)
                                            var email =
                                                alertView.findViewById(R.id.usernameLoginDialog) as EditText
                                            var password =
                                                alertView.findViewById(R.id.passwordLoginDialog) as EditText
                                            alertDialog.setView(alertView)
                                                .setNeutralButton(
                                                    "Passwort zurücksetzen",
                                                    DialogInterface.OnClickListener { dialog, which ->

                                                        GoogleFirebase.auth.sendPasswordResetEmail(
                                                            email.text.toString()
                                                        )
                                                            .addOnCompleteListener { task ->
                                                                if (task.isSuccessful) {
                                                                    Toast.makeText(
                                                                        this,
                                                                        "Email gesendet",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }

                                                    })
                                                .setPositiveButton("Login",
                                                    DialogInterface.OnClickListener { dialog, which ->


                                                        GoogleFirebase.auth.signInWithEmailAndPassword(
                                                            email.text.toString(),
                                                            password.text.toString()
                                                        )
                                                            .addOnCompleteListener(this) { task ->
                                                                if (task.isSuccessful) {
                                                                    // Sign in success, update UI with the signed-in user's information
                                                                    Log.d(
                                                                        ContentValues.TAG,
                                                                        "signInWithEmail:success"
                                                                    )
                                                                    val user =
                                                                        GoogleFirebase.auth.currentUser
                                                                    GoogleFirebase.createDBConnectionAndLoadMaterialUpdatedAt(
                                                                        object :
                                                                            FirestoreTimeCallback {
                                                                            override fun onCallback() {
                                                                                super.onCallback()
                                                                                startUp()
                                                                                loadXml()
                                                                            }

                                                                            override fun onFailureCallback() {
                                                                                super.onFailureCallback()
                                                                                Toast.makeText(
                                                                                    applicationContext,
                                                                                    "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                                                                    Toast.LENGTH_SHORT
                                                                                )
                                                                            }
                                                                        })






                                                                    actionBarDrawerToggle =
                                                                        ActionBarDrawerToggle(
                                                                            this,
                                                                            drawerLayout,
                                                                            R.string.nav_open,
                                                                            R.string.nav_close
                                                                        )

                                                                    // pass the Open and Close toggle for the drawer layout listener
                                                                    // to toggle the button
                                                                    supportActionBar!!.setDisplayHomeAsUpEnabled(
                                                                        true
                                                                    )
                                                                    supportActionBar!!.setDisplayShowTitleEnabled(
                                                                        false
                                                                    )
                                                                    // pass the Open and Close toggle for the drawer layout listener
                                                                    // to toggle the button
                                                                    drawerLayout!!.addDrawerListener(
                                                                        actionBarDrawerToggle!!
                                                                    )
                                                                    actionBarDrawerToggle!!.syncState()

                                                                    navView =
                                                                        findViewById(R.id.nav_view_admin)

                                                                    navView!!.bringToFront()
                                                                    context = applicationContext

                                                                    navView!!.setNavigationItemSelectedListener { menuItem ->
                                                                        when (menuItem.itemId) {
                                                                            R.id.itemLager -> {
                                                                                val myIntent =
                                                                                    Intent(
                                                                                        this,
                                                                                        LagerActivity::class.java
                                                                                    )
                                                                                startActivity(
                                                                                    myIntent
                                                                                )
                                                                                true
                                                                            }

                                                                            R.id.regieBericht -> {
                                                                                val myIntent =
                                                                                    Intent(
                                                                                        this,
                                                                                        MainActivity::class.java
                                                                                    )
                                                                                startActivity(
                                                                                    myIntent
                                                                                )
                                                                                true
                                                                            }
                                                                            R.id.itemAdminMaterial ->{
                                                                                StaticClass.isSelectedFromNavView = true
                                                                                val myIntent = Intent(this,AdminMaterialActivity::class.java)
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


                                                                    onButtonClickListeners()


                                                                } else {
                                                                    // If sign in fails, display a message to the user.
                                                                    Log.w(
                                                                        ContentValues.TAG,
                                                                        "signInWithEmail:failure",
                                                                        task.exception
                                                                    )
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    alertDialog.show()

                                                                }
                                                            }
                                                    })

                                            alertDialog.show()

                                        })
                                    .setPositiveButton("Login",
                                        DialogInterface.OnClickListener { dialog, which ->


                                            GoogleFirebase.auth.signInWithEmailAndPassword(
                                                email.text.toString(),
                                                password.text.toString()
                                            )
                                                .addOnCompleteListener(this) { task ->
                                                    if (task.isSuccessful) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                                                        val user = GoogleFirebase.auth.currentUser
                                                        GoogleFirebase.createDBConnectionAndLoadMaterialUpdatedAt(
                                                            object : FirestoreTimeCallback {
                                                                override fun onCallback() {
                                                                    super.onCallback()
                                                                    startUp()
                                                                    loadXml()
                                                                }

                                                                override fun onFailureCallback() {
                                                                    super.onFailureCallback()
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                                                        Toast.LENGTH_SHORT
                                                                    )
                                                                }
                                                            })






                                                        actionBarDrawerToggle =
                                                            ActionBarDrawerToggle(
                                                                this,
                                                                drawerLayout,
                                                                R.string.nav_open,
                                                                R.string.nav_close
                                                            )

                                                        // pass the Open and Close toggle for the drawer layout listener
                                                        // to toggle the button
                                                        supportActionBar!!.setDisplayHomeAsUpEnabled(
                                                            true
                                                        )
                                                        supportActionBar!!.setDisplayShowTitleEnabled(
                                                            false
                                                        )
                                                        // pass the Open and Close toggle for the drawer layout listener
                                                        // to toggle the button
                                                        drawerLayout!!.addDrawerListener(
                                                            actionBarDrawerToggle!!
                                                        )
                                                        actionBarDrawerToggle!!.syncState()

                                                        navView = findViewById(R.id.nav_view_admin)

                                                        navView!!.bringToFront()
                                                        context = applicationContext

                                                        navView!!.setNavigationItemSelectedListener { menuItem ->
                                                            when (menuItem.itemId) {
                                                                R.id.itemLager -> {
                                                                    val myIntent = Intent(
                                                                        this,
                                                                        LagerActivity::class.java
                                                                    )
                                                                    startActivity(myIntent)
                                                                    true
                                                                }

                                                                R.id.regieBericht -> {
                                                                    val myIntent = Intent(
                                                                        this,
                                                                        MainActivity::class.java
                                                                    )
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
                                                                R.id.itemAdminMaterial ->{
                                                                    StaticClass.isSelectedFromNavView = true
                                                                    val myIntent = Intent(this,AdminMaterialActivity::class.java)
                                                                    startActivity(myIntent)
                                                                    true
                                                                }

                                                                else -> {
                                                                    drawerLayout!!.closeDrawers()
                                                                    true
                                                                }
                                                            }
                                                        }

                                                        onButtonClickListeners()


                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Log.w(
                                                            ContentValues.TAG,
                                                            "signInWithEmail:failure",
                                                            task.exception
                                                        )
                                                        Toast.makeText(
                                                            applicationContext,
                                                            "Datenbank nicht erreichbar, stellen Sie eine Internetverbindung her, oder probieren Sie es später nochmal",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        alertDialog.show()

                                                    }
                                                }
                                        })

                                alertDialog.show()


                            }
                        }
                })

        alertDialog.show()

    }
    private fun setLoadingImage() {
        loadingImageView = ImageView(applicationContext)
        loadingImageView!!.setImageDrawable(getDrawable(R.drawable.loading_image))
        drawerLayout!!.addView(loadingImageView)

    }
    private fun startUp(){
        actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.nav_open,
                R.string.nav_close
            )

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout!!.addDrawerListener(
            actionBarDrawerToggle!!
        )
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        navView!!.bringToFront()
        context = applicationContext

        navView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemLager -> {
                    val myIntent = Intent(
                        this,
                        LagerActivity::class.java
                    )
                    startActivity(myIntent)
                    true
                }

                R.id.regieBericht -> {
                    val myIntent = Intent(
                        this,
                        MainActivity::class.java
                    )
                    startActivity(myIntent)
                    true
                }
                R.id.itemAdminMaterial -> {
                    StaticClass.isSelectedFromNavView = true
                    val myIntent =
                        Intent(this, AdminMaterialActivity::class.java)
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
        onButtonClickListeners()


    }

    private fun onButtonClickListeners(){

        buttonCreateWorker.setOnClickListener {
            if (editTextNewWorker.text.toString().isNotEmpty()) {
                val builder = AlertDialog.Builder(this@AdminWorkerActivity)
                builder.setTitle("Arbeiter hinzufügen")
                builder.setMessage("Möchten Sie folgenden Arbeiter hinzufügen?\n\n Name: " + editTextNewWorker.text.toString())


                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    var worker = Workers("")
                    worker.worker = editTextNewWorker.text.toString()
                    Workers.workerArray.add(worker)
                    var xmlTool = XmlTool()
                    xmlTool.saveWorksersToXml(applicationContext, Workers.workerArray)
                    GoogleFirebase.updateWorkersToDB()
                    setSpinnerContent()
                }
                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                }
                builder.show()
            }
            else{
                Toast.makeText(applicationContext,"Bitte Namen bei Neu hinzufügen",Toast.LENGTH_SHORT).show()
            }
        }


        buttonCancel.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        buttonSave.setOnClickListener{
            val builder = AlertDialog.Builder(this@AdminWorkerActivity)
            builder.setTitle("Arbeiter speichern")
            builder.setMessage("Möchten Sie folgenden Arbeiter speichern?\n\n Name: "+spinnerWorkers.selectedItem.toString())


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                var workersNew = ArrayList<Workers>()
                for (worker in Workers.workerArray){
                    if (worker.worker == spinnerWorkers.selectedItem.toString()){
                        GoogleFirebase.deleteWorkerFromDb(spinnerWorkers.selectedItem.toString())
                        var workerNew   = Workers("")
                        workerNew.worker = editTextWorker.text.toString()
                        workersNew.add(workerNew)
                    }
                    else{
                        workersNew.add(worker)
                    }
                }
                Workers.workerArray = workersNew
                var xmlTool = XmlTool()
                xmlTool.saveWorksersToXml(applicationContext,Workers.workerArray)
                GoogleFirebase.updateWorkersToDB()
                setSpinnerContent()
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }
            builder.show()
        }

        buttonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@AdminWorkerActivity)
            builder.setTitle("Arbeiter löschen")
            builder.setMessage("Möchten Sie folgenden Arbeiter löschen?\n\n Name: "+spinnerWorkers.selectedItem.toString())


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                var newWorkers = ArrayList<Workers>()

                for (worker in Workers.workerArray){
                    if (worker.worker == spinnerWorkers.selectedItem.toString()){

                    }
                    else{
                        newWorkers.add(worker)
                    }
                }
                Workers.workerArray = newWorkers
                var xmlTool = XmlTool()
                xmlTool.saveWorksersToXml(applicationContext,Workers.workerArray)
                GoogleFirebase.deleteWorkerFromDb(spinnerWorkers.selectedItem.toString())
                setSpinnerContent()
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }
            builder.show()
        }

    }
    private fun loadXml(){
        var xmlTool = XmlTool()
        xmlTool!!.loadUpdatedMaterialFromXml(applicationContext)

        if (GoogleFirebase.materialLastUpdatedDb > Times.updatedLocal.iterator().next().time) {

            if (Times.updatedLocal.isEmpty()) {
                var timarr = ArrayList<Times>()
                var time = Times(Timestamp.now())
                timarr.add(time)
                xmlTool!!.saveUpdatedMaterialToXml(timarr, applicationContext)
                Times.updatedLocal.iterator().next().time = Timestamp.now()
            }
            GoogleFirebase.loadWorkersFromDb(object : FirestoreWokersFromDbCallback {
                override fun onSuccessCallback() {
                    var timarr = ArrayList<Times>()
                    var time = Times(Timestamp.now())
                    timarr.add(time)
                    xmlTool!!.saveUpdatedMaterialToXml(timarr, applicationContext)
                    Times.updatedLocal.iterator().next().time = Timestamp.now()
                    drawerLayout!!.removeView(loadingImageView)
                    setSpinnerContent()
                }

                override fun onFailureCallback() {
                    xmlTool!!.loadWorkersFromXml(applicationContext)
                    setSpinnerContent()
                    drawerLayout!!.removeView(loadingImageView)
                }
            })
        }
        else{
            var xmlTool = XmlTool()
            xmlTool.loadWorkersFromXml(applicationContext)
            drawerLayout!!.removeView(loadingImageView)
            setSpinnerContent()
        }
    }

    private fun setSpinnerContent() {
        var workerList = ArrayList<String>()
        for (worker in Workers.workerArray){
            workerList.add(worker.worker.toString())
        }
        var dataAdapterWorker = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,workerList)
        dataAdapterWorker.setDropDownViewResource(R.layout.spinner_style)
        spinnerWorkers!!.adapter = dataAdapterWorker

        spinnerWorkers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                editTextWorker.setText(spinnerWorkers.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

}