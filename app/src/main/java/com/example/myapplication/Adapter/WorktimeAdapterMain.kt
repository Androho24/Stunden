package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Interfaces.MainActivityWorktimeInterface
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.WorktimeMain
import com.example.myapplication.R

class WorktimeAdapterMain (private var dataSet: ArrayList<WorktimeMain>, private var context: Context, private var onDeleteListener: MainActivityWorktimeInterface):RecyclerView.Adapter<WorktimeAdapterMain.ViewHolder>() {


    private var adapterItemClickListener  = onDeleteListener




    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewBegin: TextView
        val textViewEnd: TextView
        val textViewWorktime : TextView
        val textViewWR : TextView
        val textViewWorkerName : TextView

        val buttonDelete : Button
        init {
            // Define click listener for the ViewHolder's View
            textViewBegin = view.findViewById(R.id.textViewWorkTimeBeginMainAdapter)
            textViewEnd = view.findViewById(R.id.textViewWorkTimeEndAdapter)
            textViewWorktime = view.findViewById(R.id.textViewWorkTimeMainAdapter)
            textViewWR = view.findViewById(R.id.textViewWorktimeWRMainAdapter)
            textViewWorkerName = view.findViewById(R.id.textViewWorkTimeWorkerNameMainAdapter)
            buttonDelete = view.findViewById(R.id.buttonDeleteWorktimeMainAdapter)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.worktime_main_adapter, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val m = dataSet.get(position)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewBegin.text = m.beginWorktime
        viewHolder.textViewBegin.id = position
        viewHolder.textViewEnd.text = m.endWorktime
        viewHolder.textViewEnd.id = position
        viewHolder.textViewWorktime.id = position
        viewHolder.textViewWorktime.text = m.workTime
        viewHolder.textViewWR.id = position
        viewHolder.textViewWR.text = m.wegeRuest
        viewHolder.textViewWorkerName.id = position
        viewHolder.textViewWorkerName.text = m.workerName
        viewHolder.buttonDelete.id = position
        viewHolder.buttonDelete.setOnClickListener {
            var newWorkers = ArrayList<WorktimeMain>()
            for (mat in WorktimeMain.staticWorkTimeArrayList){
                if (viewHolder.textViewBegin.text == mat.beginWorktime && viewHolder.textViewEnd.text == mat.endWorktime && viewHolder.textViewWorkerName.text == mat.workerName){

                }
                else {
                    newWorkers.add(mat)
                }
            }
            WorktimeMain.staticWorkTimeArrayList = newWorkers
            onDeleteListener.onWorktimeDeletedListener()

        }

    }



    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}