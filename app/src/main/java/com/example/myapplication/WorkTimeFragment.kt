package com.example.myapplication

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
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment


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
            if (checkBoxWorker1!!.isChecked) {
                worker.add(checkBoxWorker1!!.text.toString())
            }
            if (checkBoxPause!!.isChecked) {
                worker.add(checkBoxPause!!.text.toString())
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

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        buttonSave = view.findViewById(R.id.buttonSaveWorktime)
        buttonBeginWorktime = view.findViewById(R.id.buttonEditBeginWorktime)
        buttonEditEndWorktime = view.findViewById(R.id.buttonEditEndWorktime)
        buttonEditWegeRuest = view.findViewById(R.id.buttonEditEegeRuestWorktime)
        checkBoxWorker1 = view.findViewById(R.id.checkBoxWorker1)
        checkBoxPause = view.findViewById(R.id.checkBoxPause)
        buttonCancel = view.findViewById(R.id.buttonCancelWorktime)
        textBeginWorktime = view.findViewById(R.id.textViewBeginWorktime)
        textEndWorktime = view.findViewById(R.id.textViewEndWorktime)
        textWegeRuest = view.findViewById(R.id.editTextWegeRuestWorktime)


        setOnButtonClickListerns()
        // Fetch arguments from bundle and set title

        val title = requireArguments().getString("title", "Enter Name")
        dialog!!.setTitle(title)
        getDialog()?.getWindow()?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
    }

    companion object {
        fun newInstance(title: String?): WorkTimeFragment {
            val frag = WorkTimeFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.setArguments(args)
            return frag
        }
    }
}