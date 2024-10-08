package com.androho.elektroeibauer.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.androho.elektroeibauer.Lager.LagerActivity
import com.androho.elektroeibauer.Material.EditMaterialList
import com.androho.elektroeibauer.Objects.CustomerMaterial
import com.androho.elektroeibauer.Objects.Material
import com.androho.elektroeibauer.R
import com.androho.elektroeibauer.Static.StaticClass

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
            var textView2Width = Math.round(((StaticClass.displayWidth-150)*0.71).toFloat())-30-20
            var editTextWidth = Math.round(((StaticClass.displayWidth-150)*0.15).toFloat())
            var textView1Width = Math.round(((StaticClass.displayWidth-150)*0.15).toFloat())
            // Define click listener for the ViewHolder's View
            textView1 = view.findViewById(R.id.textViewUnitAddLagerAdapter)
            var textView1Params = FrameLayout.LayoutParams(textView1Width,FrameLayout.LayoutParams.MATCH_PARENT)
            textView1Params.marginStart= textView2Width+editTextWidth

            textView2 = view.findViewById(R.id.textViewNameAddLagerAdapter)
            var textView2Params = FrameLayout.LayoutParams(textView2Width,LayoutParams.MATCH_PARENT)
            textView2Params.marginStart= 30
            textView2.layoutParams = textView2Params


            editText = view.findViewById(R.id.editTextAmountAddLagerAdapter)
            var editTextParams = FrameLayout.LayoutParams(editTextWidth,LayoutParams.MATCH_PARENT)
            editTextParams.marginStart = textView2Width
            editText.layoutParams = editTextParams

            button = view.findViewById(R.id.buttonAddAddLagerAdapter)
            var buttonParams = FrameLayout.LayoutParams(150,LayoutParams.MATCH_PARENT)
            buttonParams.marginStart = textView2Width+textView1Width+editTextWidth
            button.layoutParams = buttonParams

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
                newMaterial.materialAmount = viewHolder.editText.text.toString().toFloat().toString()
                CustomerMaterial.customerMaterialsLager.add(newMaterial)
                Toast.makeText(context, "Material hinzugefügt", Toast.LENGTH_SHORT).show()
            }
        }
        viewHolder.textView1.setOnClickListener (object :View.OnClickListener{
            override fun onClick(v: View?) {
                var isInMaterial = false
                for (mat in Material.materials) {
                    if (viewHolder.textView1.text.toString() == mat.unit && viewHolder.textView2.text.toString() == mat.material) {
                        isInMaterial = true
                    }
                }

                if (isInMaterial == true) {
                    val builder = AlertDialog.Builder(v!!.context)
                    builder.setTitle("Material ändern")
                    builder.setMessage("Dieses Material kann nicht geändert werden!")


                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                        isInMaterial = false
                    }


                    builder.show()
                }else{
                    var activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity,EditMaterialList::class.java)
                    intent.putExtra("unit",viewHolder.textView1.text.toString())
                    intent.putExtra("name",viewHolder.textView2.text.toString())
                    activity.startActivityForResult(intent,LagerActivity.materialEditList)
                }
            }

        })
        viewHolder.textView2.setOnClickListener (object :View.OnClickListener{
            override fun onClick(v: View?) {
                var isInMaterial = false
                for (mat in Material.materials) {
                    if (viewHolder.textView1.text.toString() == mat.unit && viewHolder.textView2.text.toString() == mat.material) {
                        isInMaterial = true
                    }
                }

                if (isInMaterial == true) {
                    val builder = AlertDialog.Builder(v!!.context)
                    builder.setTitle("Material ändern")
                    builder.setMessage("Dieses Material kann nicht geändert werden!")


                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                        isInMaterial = false
                    }


                    builder.show()
                }else{
                    var activity = v!!.context as AppCompatActivity
                    var intent = Intent(activity,EditMaterialList::class.java)
                    intent.putExtra("unit",viewHolder.textView1.text.toString())
                    intent.putExtra("name",viewHolder.textView2.text.toString())
                    activity.startActivityForResult(intent,LagerActivity.materialEditList)
                }

            }
        })

    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size



}