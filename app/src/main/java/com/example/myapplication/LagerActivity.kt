package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.Lager.AddMaterialLagerActivity
import com.example.myapplication.Objects.Customer
import com.google.android.material.navigation.NavigationView


class LagerActivity : AppCompatActivity() {

    var drawerLayout : DrawerLayout? = null
    var navView : NavigationView? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var spinnerCustomer : Spinner? = null
    var tableMaterial : TableLayout? = null
    var buttonAddMaterial: Button? = null
    var buttonPreview: Button? = null
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lager)

        drawerLayout = findViewById(R.id.drawer_layout_lager)
        spinnerCustomer= findViewById(R.id.spinnerCustomerLager)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialLager)

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


        navView = findViewById(R.id.nav_view_lager)


        navView!!.bringToFront();

        navView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.regieBericht -> {
                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                    true
                }
                else -> {
                    Toast.makeText(this,"hello", Toast.LENGTH_SHORT)
                    false
                }
            }
        }

        setButtonOnClickListeners()
        setCustomerSpinner()

    }

    private fun setButtonOnClickListeners() {
        buttonAddMaterial!!.setOnClickListener{
            var intent = Intent(this,AddMaterialLagerActivity::class.java)
            startActivityForResult(intent, materialresult)
        }
    }

    private fun setCustomerSpinner() {
        var customerList = ArrayList<String>()
        for (customer in Customer.arrayCustomers) {
            customerList.add(customer.name + ", " + customer.preName + ", " + customer.streetName)
        }
        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, customerList!!)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerCustomer!!.adapter = dataAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == materialresult){

        }
    }

    companion object{
        var materialresult = 1
    }
}