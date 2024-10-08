package com.androho.elektroeibauer.Admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androho.elektroeibauer.Database.GoogleFirebase
import com.androho.elektroeibauer.Objects.Material
import com.androho.elektroeibauer.R
import com.androho.elektroeibauer.XmlTool

class AdminUploadCustomMaterialsActivity : AppCompatActivity() {

    var tableCustomMat: RecyclerView? = null
    var buttonCancel: Button? = null
    var buttonAdd: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_add_upload_custommat_activity)
        tableCustomMat = findViewById(R.id.recycleViewAdminCustomUpAddMatAt)
        buttonAdd = findViewById(R.id.buttonAddAdminCustUpMatAt)
        buttonCancel = findViewById(R.id.buttonCancelAdminCustUpMatAt)
        tableCustomMat!!.layoutManager = LinearLayoutManager(this)
        if (!Material.ownMaterials.isEmpty()) {
            Material.adminCustList = ArrayList<Material>()
            Material.adminCustList.addAll(Material.ownMaterials)
        }
        var adapter = MaterialAdapterCustAdaper(
            Material.adminCustList,
            applicationContext
        )
        tableCustomMat!!.adapter = adapter

        setButtonOnClickListeners()
    }

    private fun setButtonOnClickListeners() {
        buttonCancel!!.setOnClickListener { finish() }

        buttonAdd!!.setOnClickListener {

            for (matCust in Material.adminCustList) {
                var exits = false
                for (mat in Material.materials) {
                    if(mat.material == matCust.material){
                        exits = true
                    }
                }

                if (exits == false) {
                    var id = 0;
                    for (mat in Material.materials) {
                        if (id < mat.id) {
                            id = mat.id
                        }
                    }
                    matCust.id = id + 1

                    var newOwnMat = ArrayList<Material>()
                    for (ownMat in Material.ownMaterials) {
                        if (ownMat.material == matCust.material) {

                        } else {
                            newOwnMat.add(ownMat)
                        }
                    }
                    Material.ownMaterials = newOwnMat
                    Material.adminCustList = newOwnMat
                    Material.materials.add(matCust)
                }else{

                }



            }
            var xmlTool = XmlTool()
            xmlTool.saveOwnMaterialsToXml(Material.ownMaterials,applicationContext)
            xmlTool.saveMaterialsToXml(Material.materials,applicationContext)
            tableCustomMat!!.removeAllViews()
            tableCustomMat!!.layoutManager = LinearLayoutManager(this)
            if (!Material.ownMaterials.isEmpty()) {
                Material.adminCustList = ArrayList<Material>()
                Material.adminCustList.addAll(Material.ownMaterials)
            }
            var adapter = MaterialAdapterCustAdaper(
                Material.adminCustList,
                applicationContext
            )
            tableCustomMat!!.adapter = adapter
            GoogleFirebase.updateMaterialToDatabase()
        }
    }


    inner class MaterialAdapterCustAdaper(
        private var dataSet: ArrayList<Material>,
        private var context: Context
    ) :
        RecyclerView.Adapter<MaterialAdapterCustAdaper.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView1: TextView
            val textView2: TextView

            val button: Button


            init {
                // Define click listener for the ViewHolder's View
                textView1 = view.findViewById(R.id.textViewUnitAdminAddCustomMat)
                textView2 = view.findViewById(R.id.textViewNameAdminAddCustomMat)
                button = view.findViewById(R.id.buttonDeleteAdminAddCustomMat)


            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.admin_custom_mat_list, viewGroup, false)

            return ViewHolder(view)
        }


        override fun getItemCount() = dataSet.size


        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val m = dataSet.get(position)
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.textView1.text = m.unit
            viewHolder.textView1.id = position
            viewHolder.textView1.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    var activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, AdminMaterialEditActivity::class.java)
                    intent.putExtra("unit", viewHolder.textView1.text.toString())
                    intent.putExtra("name", viewHolder.textView2.text.toString())
                    intent.putExtra("ownMat", true)
                    activity.startActivityForResult(intent, custEditResult)
                }

            })
            viewHolder.textView2.text = m.material
            viewHolder.textView2.id = position

            viewHolder.textView2.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    var activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity, AdminMaterialEditActivity::class.java)
                    intent.putExtra("unit", viewHolder.textView1.text.toString())
                    intent.putExtra("name", viewHolder.textView2.text.toString())
                    intent.putExtra("ownMat", true)
                    activity.startActivityForResult(intent, custEditResult)
                }

            })
            viewHolder.button.id = position
            viewHolder.button.setOnClickListener {
                var deletedList = ArrayList<Material>()
                for (mat in Material.adminCustList){
                    if (viewHolder.textView2.text == mat.material){

                    }
                    else{
                        deletedList.add(mat)
                    }
                }
                Material.adminCustList = deletedList
                var adapter = MaterialAdapterCustAdaper(
                    Material.adminCustList,
                    applicationContext
                )
                tableCustomMat!!.adapter = adapter

            }
        }


    }
    companion object{
        var custEditResult = 10007
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == custEditResult){
            var adapter = MaterialAdapterCustAdaper(
                Material.adminCustList,
                applicationContext
            )
            tableCustomMat!!.adapter = adapter
        }
    }
}
