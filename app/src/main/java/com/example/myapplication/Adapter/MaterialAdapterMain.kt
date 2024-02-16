package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Interfaces.MainActivityMatInterface
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.R


class MaterialAdapterMain(private var dataSet: ArrayList<CustomerMaterial>, private var context: Context,private var onDeleteListener: MainActivityMatInterface):RecyclerView.Adapter<MaterialAdapterMain.ViewHolder>() {


    private var adapterItemClickListener  = onDeleteListener




    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewAmount: TextView
        val textViewUnit: TextView
        val textViewName : TextView
        val buttonDelete : Button
        init {
            // Define click listener for the ViewHolder's View
            textViewAmount = view.findViewById(R.id.textViewMatAmountMain)
            textViewUnit = view.findViewById(R.id.textViewMatUnitMain)
            textViewName = view.findViewById(R.id.textViewMaterialNameMain)
            buttonDelete = view.findViewById(R.id.buttonDeleteMatMain)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.material_adapter_main, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val m = dataSet.get(position)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewAmount.text = m.materialAmount
        viewHolder.textViewAmount.id = position
        viewHolder.textViewUnit.text = m.materialUnit
        viewHolder.textViewUnit.id = position
        viewHolder.textViewName.id = position
        viewHolder.textViewName.text = m.materialName
        viewHolder.buttonDelete.id = position
       viewHolder.buttonDelete.setOnClickListener {
           var newMats = ArrayList<CustomerMaterial>()
           for (mat in CustomerMaterial.customerMaterials){
               if (viewHolder.textViewName.text == mat.materialName){

               }
               else {
                   newMats.add(mat)
               }
           }
            CustomerMaterial.customerMaterials = newMats
           adapterItemClickListener.onMaterialDeletedListener()




        }

    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}