package com.example.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Lager.LagerActivity
import com.example.myapplication.Material.EditMaterialList
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.Material
import com.example.myapplication.R

class MaterialAdapterAddLager (private var dataSet: ArrayList<Material>,private var context: Context) :
RecyclerView.Adapter<MaterialAdapterAddLager.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
                newMaterial.materialAmount = viewHolder.editText.text.toString()
                CustomerMaterial.customerMaterialsLager.add(newMaterial)
                Toast.makeText(context, "Material hinzugefügt", Toast.LENGTH_SHORT).show()
            }
        }
        viewHolder.textView1.setOnClickListener (object :View.OnClickListener{
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                var intent = Intent(activity, EditMaterialList::class.java)
                intent.putExtra("unit", viewHolder.textView1.text.toString())
                intent.putExtra("name", viewHolder.textView2.text.toString())
                activity.startActivityForResult(intent, LagerActivity.materialEditList)
            }

        })
        viewHolder.textView2.setOnClickListener (object :View.OnClickListener{
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                var intent = Intent(activity,EditMaterialList::class.java)
                intent.putExtra("unit",viewHolder.textView1.text.toString())
                intent.putExtra("name",viewHolder.textView2.text.toString())
                activity.startActivityForResult(intent,LagerActivity.materialEditList)

            }
        })

    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size



}