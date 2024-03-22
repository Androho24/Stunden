package com.example.myapplication.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter

import com.example.myapplication.R
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Barcode.CaptureAct
import com.example.myapplication.Database.GoogleFirebase
import com.example.myapplication.Lager.LagerActivity
import com.example.myapplication.Objects.Material
import com.example.myapplication.XmlTool
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class AdminAddNewMaterialActivity : AppCompatActivity() {

    var spinnerUnits : Spinner?= null
    var editTextMatName : EditText? = null
    var textViewBarcode : TextView? = null
    var editTextAmount : EditText? = null
    var checkBoxOrder : CheckBox? = null

    var buttonAddCustomMaterials : Button? = null
    var buttonAddBarcode : Button? = null
    var buttonDeleteBarcode : Button? = null
    var buttonCancel : Button? = null
    var buttonAddMaterial : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_add_new_material_activity)
        spinnerUnits = findViewById(R.id.spinnerUnitAdminNewMatAt)
        editTextMatName = findViewById(R.id.editTextMatNameAdminAddMatAt)
        textViewBarcode = findViewById(R.id.textViewBarcodeAdminAddMatAt)
        editTextAmount = findViewById(R.id.editTextAmountAdminAddMatAt)
        checkBoxOrder = findViewById(R.id.checkBoxAdminAddMatAt)
        buttonAddBarcode = findViewById(R.id.buttonAddBarcodeAdminAddMatAt)
        buttonDeleteBarcode = findViewById(R.id.buttonDeleteBarcodeAdminAddMatAt)
        buttonCancel = findViewById(R.id.buttonAddMatAdminAddMatAt)
        buttonAddMaterial = findViewById(R.id.buttonAddMatAdminAddMatAt)
        buttonAddCustomMaterials = findViewById(R.id.buttonAddCustomMatsAdminNewMatAt)

        setSpinnerContent()
        buttonSetOnClisteners()


    }

    private fun setSpinnerContent() {
        var unitList = ArrayList<String>()
        unitList.add("Stck")
        unitList.add("m")

        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitList)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerUnits!!.adapter = dataAdapter
    }

    private fun buttonSetOnClisteners() {

        buttonAddCustomMaterials!!.setOnClickListener {
            var intent = Intent(this,AdminUploadCustomMaterialsActivity::class.java)
            startActivity(intent)

        }
        buttonDeleteBarcode!!.setOnClickListener {
            textViewBarcode!!.setText("")
        }

        buttonAddBarcode!!.setOnClickListener {
            scanCode()
        }
        buttonAddMaterial!!.setOnClickListener {

            if (editTextMatName!!.text.equals("")){
                Toast.makeText(this,"Bitte Materialnamen hinzufügen",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (spinnerUnits!!.selectedItem.toString().equals("")){
                Toast.makeText(this,"Bitte Einheit auswählen",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var materialExists = false
            for (mat in Material.materials){
                if (mat.material ==editTextMatName!!.text.toString()  ){
                    Toast.makeText(this,"Material existiert bereits",Toast.LENGTH_SHORT).show()
                    materialExists = true
                    return@setOnClickListener
                }
            }

            if (!materialExists){
                var id = 0
                for (mat in Material.materials){
                    if (mat.id > id){
                        id = mat.id
                    }
                }

                var newMat = Material()

                newMat.material = editTextMatName!!.text.toString()
                var amount = 0
                if (editTextAmount!!.text.equals("")){
                    amount = 0
                }
                else{
                    editTextAmount!!.text.toString().toInt()
                }
                newMat.anzahl = amount
                newMat.unit = spinnerUnits!!.selectedItem.toString()
                newMat.bestellen = checkBoxOrder!!.isChecked
                newMat.id = id+1
                newMat.barcode = textViewBarcode!!.text.toString()
                Material.materials.add(newMat)
                var xmlTool = XmlTool()
                xmlTool.saveMaterialsToXml(Material.materials,applicationContext)
                GoogleFirebase.updateMaterialToDatabase()
                var intent = Intent()
                setResult(AdminMaterialActivity.newMaterialResult,intent)
                finish()

            }
        }
    }

    private fun scanCode() {
        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setCaptureActivity(CaptureAct::class.java)
        barLauncher.launch(options)
    }
    var barLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult? ->


        if (result!!.contents != null) {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Result").setMessage(result.contents).setPositiveButton(
                "ok"
            ) { _, _ ->
                var i = 0
                textViewBarcode!!.text = result.contents.toString()


            }.show()
        }
    }
}