package com.androho.elektroeibauer.Adapter


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.androho.elektroeibauer.Objects.Material
import com.androho.elektroeibauer.Lager.LagerActivity
import com.androho.elektroeibauer.Material.EditMaterialList
import com.androho.elektroeibauer.Objects.CustomerMaterial
import com.androho.elektroeibauer.R
import com.androho.elektroeibauer.Static.StaticClass


class MaterialAdapter(private var dataSet: ArrayList<Material>, private var context: Context) :
    RecyclerView.Adapter<com.androho.elektroeibauer.Adapter.MaterialAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView
        val textView2: TextView
        val editText : EditText
        val button : Button
        var checkBox:CheckBox
        init {
            var editTextWidth =Math.round(((StaticClass.displayWidth-270)*0.18).toFloat())
            // Define click listener for the ViewHolder's View
            var widthTextView2 = Math.round(((StaticClass.displayWidth-270)*0.64).toFloat())-30-20


            textView1 = view.findViewById(R.id.textView1)
            var widthTextView1 = Math.round(((StaticClass.displayWidth-270)*0.18).toFloat())
            var params = FrameLayout.LayoutParams(widthTextView1, FrameLayout.LayoutParams.MATCH_PARENT)
            params.marginStart = widthTextView2+120+editTextWidth
            textView1.layoutParams = params



            var textView2Params =  FrameLayout.LayoutParams(widthTextView2, FrameLayout.LayoutParams.MATCH_PARENT)
            textView2 = view.findViewById(R.id.textView2)
            textView2Params.marginStart = 30
            textView2.layoutParams = textView2Params

            editText = view.findViewById(R.id.editTextHolder)
            var editTextParams = FrameLayout.LayoutParams(editTextWidth,FrameLayout.LayoutParams.MATCH_PARENT)
            editTextParams.marginStart = widthTextView2 + 120
            editText.layoutParams = editTextParams

            button = view.findViewById(R.id.buttonHolder)
            var buttonParams = FrameLayout.LayoutParams(150,FrameLayout.LayoutParams.MATCH_PARENT)
            buttonParams.marginStart = widthTextView2+widthTextView1+editTextWidth+120
            button.layoutParams = buttonParams

            checkBox = view.findViewById(R.id.checkBoxMaterialAdapterZugang)
            var checkBoxParams = FrameLayout.LayoutParams(120, FrameLayout.LayoutParams.MATCH_PARENT)
            checkBoxParams.marginStart = widthTextView2+20
            checkBox.layoutParams = checkBoxParams
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): com.androho.elektroeibauer.Adapter.MaterialAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.material_row_adapter_mat_act, viewGroup, false)

        return com.androho.elektroeibauer.Adapter.MaterialAdapter.ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: com.androho.elektroeibauer.Adapter.MaterialAdapter.ViewHolder, position: Int) {
        val m = dataSet.get(position)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView1.text = m.unit
        viewHolder.textView1.id = position
        viewHolder.textView2.text = m.material
        viewHolder.textView2.id = position
        viewHolder.editText.id = position
        viewHolder.checkBox.id = position
        viewHolder.button.id = position
        viewHolder.button.setOnClickListener {
            if (viewHolder.editText.text.toString() != "") {
                var newMaterial = CustomerMaterial()
                newMaterial.materialName = viewHolder.textView2.text.toString()
                newMaterial.materialUnit = viewHolder.textView1.text.toString()
                if (viewHolder.checkBox.isChecked){
                    newMaterial.materialAmount = "+"+(Math.abs(viewHolder.editText.text.toString().toFloat())).toString()

                    newMaterial.materialZugang = true
                }
                else{
                    var amou = viewHolder.editText.text.toString().toFloat().toString()
                    newMaterial.materialZugang = false
                    newMaterial.materialAmount = amou
                }
                CustomerMaterial.customerMaterials.add(newMaterial)
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
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

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
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

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