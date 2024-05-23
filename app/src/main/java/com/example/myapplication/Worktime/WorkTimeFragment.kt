package com.example.myapplication.Worktime

import android.app.Activity
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myapplication.Objects.Workers
import com.example.myapplication.R
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat


class WorkTimeFragment : DialogFragment() {

    var textBeginWorktime: TextView? = null
    var textEndWorktime: TextView? = null
    var textWegeRuest: TextView? = null
    var checkBoxWorker1: CheckBox? = null
    var checkBoxPause: CheckBox? = null

    var buttonBeginWorktime: Button? = null
    var buttonEditEndWorktime: Button? = null
    var buttonEditWegeRuest: Button? = null
    var buttonSave: Button? = null
    var buttonCancel: Button? = null

    var tableWorkers: TableLayout? = null


    interface onWorktimeEventLisnter {
        fun worktimeListner(
            beginWorktime: String,
            endWorkTime: String,
            wegeRuest: String,
            workers: ArrayList<String>
        )
    }

    var worktimeLister: onWorktimeEventLisnter? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            worktimeLister = activity as onWorktimeEventLisnter
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        return inflater.inflate(R.layout.worktime_fragment, container)
    }


    private fun setOnButtonClickListerns() {
        buttonSave!!.setOnClickListener {
            var worker = ArrayList<String>()
            var i = 0
            for (workers in Workers.workerArray) {
                var row = tableWorkers!!.getChildAt(i) as TableRow
                var checkbox = row.getChildAt(0) as CheckBox
                if (checkbox.isChecked) {
                    worker.add(checkbox.text.toString())

                }

                i++
            }

            try {
                var formattedDate = SimpleDateFormat(textBeginWorktime!!.text.toString())
            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, "Bitte Arbeitsbeginn hinzufügen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                var formated = SimpleDateFormat(textEndWorktime!!.text.toString())
            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, "Bitte Arbeitsende hinzufügen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (worker.size == 0) {
                Toast.makeText(context, "Bitte Arbeitnehmer auswählen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            if (textEndWorktime!!.text.toString() == "") {
                Toast.makeText(context, "Bitte Arbeitsende hinzufügen", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            worktimeLister!!.worktimeListner(
                textBeginWorktime!!.text.toString(),
                textEndWorktime!!.text!!.toString(),
                textWegeRuest!!.text.toString(),
                worker
            )
            this.dismiss()
        }
        buttonBeginWorktime!!.setOnClickListener {
            val picker: TimePickerDialog

            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)


            // time picker dialog
            // time picker dialog
            picker = TimePickerDialog(
                view?.context,
                { tp, sHour, sMinute ->
                    textBeginWorktime!!.text = String.format("%02d:%02d", sHour, sMinute)
                }, hour, minutes, true
            )
            picker.show()

        }

        buttonEditEndWorktime!!.setOnClickListener {
            val picker: TimePickerDialog

            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)


            // time picker dialog
            // time picker dialog
            picker = TimePickerDialog(
                view?.context,
                { tp, sHour, sMinute ->
                    textEndWorktime!!.text = String.format("%02d:%02d", sHour, sMinute)
                }, hour, minutes, true
            )
            picker.show()
        }

        buttonEditWegeRuest!!.setOnClickListener {
            textWegeRuest!!.isCursorVisible = true
            textWegeRuest!!.isFocusableInTouchMode = true

            textWegeRuest!!.inputType = InputType.TYPE_CLASS_NUMBER
            textWegeRuest!!.requestFocus()
        }
        buttonCancel!!.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        buttonSave = view.findViewById(R.id.buttonSaveWorktime)
        buttonBeginWorktime = view.findViewById(R.id.buttonEditBeginWorktime)
        buttonEditEndWorktime = view.findViewById(R.id.buttonEditEndWorktime)
        buttonEditWegeRuest = view.findViewById(R.id.buttonEditEegeRuestWorktime)
        buttonCancel = view.findViewById(R.id.buttonCancelWorktime)
        textBeginWorktime = view.findViewById(R.id.textViewBeginWorktime)
        textEndWorktime = view.findViewById(R.id.textViewEndWorktime)
        textWegeRuest = view.findViewById(R.id.editTextWegeRuestWorktime)
        tableWorkers = view.findViewById(R.id.tableLayoutWorktime)
        var heigt = tableWorkers!!.height
        var sadf = heigt
        setOnButtonClickListerns()

        setUpTableWorker()
        // Fetch arguments from bundle and set title

        val title = requireArguments().getString("title", "Enter Name")
        dialog!!.setTitle(title)
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
    }

    private fun setUpTableWorker() {

        tableWorkers!!.removeAllViews()

        for (workers in Workers.workerArray) {
            var row = TableRow(activity)
            val checkBox = CheckBox(activity)
            checkBox.text = workers.worker.toString()
            row.addView(checkBox)

            tableWorkers!!.addView(row)
        }

        var heigt = tableWorkers!!.height
        var sadf = heigt

    }

    companion object {
        fun newInstance(title: String?): WorkTimeFragment {
            val frag = WorkTimeFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }
}