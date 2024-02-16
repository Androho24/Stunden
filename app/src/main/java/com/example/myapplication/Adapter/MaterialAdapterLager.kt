package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Interfaces.LagerActivityInterface
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.R

class MaterialAdapterLager(private var dataSet: ArrayList<CustomerMaterial>, onDeletedListener:LagerActivityInterface) :
    RecyclerView.Adapter<MaterialAdapterLager.ViewHolder>() {

        private var onDeletedClickListener = onDeletedListener

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        viewHolder.textViewUnit.id = position
        viewHolder.textViewUnit.text = m.materialUnit
        viewHolder.textViewName.id = position
        viewHolder.textViewName.text = m.materialName
        viewHolder.buttonDelete.id = position
        viewHolder.buttonDelete.setOnClickListener {
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
    }

    // Replace the contents of a view (invoked by the layout manager)




    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size



}