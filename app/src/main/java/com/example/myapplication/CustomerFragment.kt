package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.Objects.Customer
import com.example.myapplication.Objects.CustomerExpanded
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.WorktimeMain
import java.util.UUID

class CustomerFragment : DialogFragment(), CustomerClientFragment.onClientEventListener {

    var editPrename: EditText? = null
    var editName: EditText? = null
    var editStreetName: EditText? = null
    var editStreetNumber: EditText? = null
    var editPlz: EditText? = null
    var editLocation: EditText? = null
    var editProjectNumber: EditText? = null
    var buttonSaveCustomer: Button? = null
    var buttonDelete : Button? = null
    var spinnerCustomers: Spinner? = null
    var customerSelected = false
    var customerIdForEdit = ""
    var buttonClearText: Button? = null
    var buttonAddClient : Button? = null
    var buttonCancel : Button? = null
    var buttonCreateNewCustomer : Button? = null


    interface onNewCustomerEventListener {
        fun onNewCustomerListener()
    }

    var newCustomerListener: onNewCustomerEventListener? = null
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            newCustomerListener = activity as onNewCustomerEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        newCustomerListener!!.onNewCustomerListener()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        return inflater.inflate(R.layout.customer_fragment, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        var windowManager : WindowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var display = windowManager.defaultDisplay
        var lp : FrameLayout.LayoutParams = FrameLayout.LayoutParams(display.width,display.height)
        view.layoutParams = lp
        // Fetch arguments from bundle and set title
        editLocation = view.findViewById(R.id.editTextLocationCustomer)
        editName = view.findViewById(R.id.editTextNachnameCustomer)
        editPlz = view.findViewById(R.id.editTextPlzCustomer)
        editPrename = view.findViewById(R.id.editTextVornameCustomer)
        editStreetName = view.findViewById(R.id.editTextStreetCustomer)
        editStreetNumber = view.findViewById(R.id.editTextStreetNumberCustomer)
        editProjectNumber = view.findViewById(R.id.editTextProjectNumber)
        buttonSaveCustomer = view.findViewById(R.id.buttonNewCustomerCustomer)
        spinnerCustomers = view.findViewById(R.id.spinnerCustomerCustomer)
        buttonClearText = view.findViewById(R.id.buttonNewClearCustomer)
        buttonAddClient = view.findViewById(R.id.buttonAddExpandedCustomerCustomer)
        buttonDelete = view.findViewById(R.id.buttonDeleteCustomerFrag)
        buttonCancel = view.findViewById(R.id.buttonCancelClientFragment)
        buttonCreateNewCustomer = view.findViewById(R.id.buttonCreateNewCustomer)

        val title = requireArguments().getString("title", "Enter Name")
        dialog!!.setTitle(title)
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )

        setSpinnerCustomerContent()
        setSpinnerOnClickListener()
        onButtonClickListeners()
    }

    private fun setSpinnerOnClickListener() {
        spinnerCustomers!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                im: Int,
                l: Long
            ) {
                if (im >= 0) {
                    if (spinnerCustomers!!.selectedItem.toString() != "Bauvorhaben") {
                        var expanedCustome = CustomerExpanded("", "", "", "", "", "")
                        var selectedCustomer =
                            Customer("", "", "", "", "", "", "", "", expanedCustome)
                        var spinnerStringCustomer = spinnerCustomers!!.selectedItem.toString()
                        var name = spinnerStringCustomer.split(", ")
                        if (name.lastIndex == 2) {
                            for (customer in Customer.arrayCustomers) {
                                if (customer.name == name[0] && customer.preName == name[1] && customer.streetName == name[2]) {
                                    selectedCustomer = customer
                                }
                            }
                        } else if (name.lastIndex == 1) {
                            for (customer in Customer.arrayCustomers) {
                                if (customer.name == name[0] && customer.preName == name[1]) {
                                    selectedCustomer = customer
                                }
                            }
                            for (customer in Customer.arrayCustomers) {
                                if (customer.name == name[0] && customer.streetName == name[1]) {
                                    selectedCustomer = customer
                                }
                            }
                        } else if (name.lastIndex == 0) {
                            for (customer in Customer.arrayCustomers) {
                                if (customer.name == name[0]) {
                                    selectedCustomer = customer
                                }
                            }
                        }

                        if (selectedCustomer.name != "") {
                            editProjectNumber!!.setText(selectedCustomer.projectNumber)
                            editPlz!!.setText(selectedCustomer.plz)
                            editPrename!!.setText(selectedCustomer.preName)
                            editName!!.setText(selectedCustomer.name)
                            editStreetNumber!!.setText(selectedCustomer.streetNumber)
                            editStreetName!!.setText(selectedCustomer.streetName)
                            editLocation!!.setText(selectedCustomer.location)
                            customerIdForEdit = selectedCustomer.customerId
                        }

                    }
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun setSpinnerCustomerContent() {
        var customerList = ArrayList<String>()
        customerList.add("Bauvorhaben")
        for (customer in Customer.arrayCustomers) {
            customerList.add(customer.name + ", " + customer.preName + ", " + customer.streetName)
        }
        val dataAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            customerList
        )
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerCustomers!!.adapter = dataAdapter
    }

    private fun onButtonClickListeners() {

        buttonAddClient!!.setOnClickListener {


            if (editName!!.text.toString() != "") {
                if (customerIdForEdit != "") {
                    for (customer in Customer.arrayCustomers) {
                        if (customer.customerId == customerIdForEdit) {


                            val bundle = Bundle()
                            bundle.putString("customerid", customer.customerId)

                            val fm: FragmentManager = requireActivity().supportFragmentManager
                            val customerFragmentDialog: CustomerClientFragment =
                                CustomerClientFragment.newInstance("Some Title")
                            customerFragmentDialog.arguments = bundle
                            fm.beginTransaction().add(customerFragmentDialog, "some Dialog")
                                .commit()


                        }
                    }

                } else {
                    var uudi = UUID.randomUUID()
                    var customerExpanded = CustomerExpanded("", "", "", "", "", "")
                    var newCustomer = Customer(
                        uudi.toString(),
                        editName!!.text.toString(),
                        editPrename!!.text.toString(),
                        editStreetName!!.text.toString(),
                        editStreetNumber!!.text.toString(),
                        editPlz!!.text.toString(),
                        editLocation!!.text.toString(),
                        editProjectNumber!!.text.toString(),
                        customerExpanded
                    )
                    Customer.arrayCustomers.add(newCustomer)
                    Customer.arrayCustomers.sortBy { list -> list.name }
                    var xmlTool = XmlTool()
                    xmlTool.saveProfilesToXml(Customer.arrayCustomers, requireContext())
                    val bundle = Bundle()
                    bundle.putString("customerid", newCustomer.customerId)

                    val fm: FragmentManager = requireActivity().supportFragmentManager
                    val customerFragmentDialog: CustomerClientFragment =
                        CustomerClientFragment.newInstance("Some Title")
                    customerFragmentDialog.arguments = bundle
                    fm.beginTransaction().add(customerFragmentDialog, "some Dialog").commit()
                }
            }else{
                Toast.makeText(context, "Bitte Nachnamen hinzufügen",Toast.LENGTH_SHORT).show()
            }
        }

        buttonDelete!!.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Kunde löschen")
            builder.setMessage("Wollen Sie wirklich den Kunden löschen?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                var newCustomers = ArrayList<Customer>()
                for (cust in Customer.arrayCustomers){
                    if (editName!!.text.toString() == cust.name && editPrename!!.text.toString() == cust.preName && editStreetName!!.text.toString() == cust.streetName){

                    }
                    else{
                        newCustomers.add(cust)
                    }

                    Customer.arrayCustomers = newCustomers
                    setSpinnerCustomerContent()

                }
                var xmlTool = XmlTool()
                xmlTool.saveProfilesToXml(Customer.arrayCustomers, requireContext())
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()


        }

        buttonClearText!!.setOnClickListener {


            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Eingaben löschen")
            builder.setMessage("Wollen Sie wirklich alle Eingaben löschen?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                editLocation!!.setText("")
                editName!!.setText("")
                editPlz!!.setText("")
                editPrename!!.setText("")
                editStreetName!!.setText("")
                editStreetNumber!!.setText("")
                editProjectNumber!!.setText("")

                customerIdForEdit = ""
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()




        }

        buttonSaveCustomer!!.setOnClickListener {
            if (!editName!!.text.toString().isEmpty() && !editPrename!!.text.toString().isEmpty()) {
                if (customerIdForEdit != "") {
                    for (customer in Customer.arrayCustomers) {
                        if (customer.customerId == customerIdForEdit) {
                            customer.name = editName!!.text.toString()
                            customer.preName = editPrename!!.text.toString()
                            customer.streetName = editStreetName!!.text.toString()
                            customer.streetNumber = editStreetNumber!!.text.toString()
                            customer.plz = editPlz!!.text.toString()
                            customer.location = editLocation!!.text.toString()
                            customer.projectNumber = editProjectNumber!!.text.toString()
                            //  Customer.arrayCustomers.add(customer)


                        }

                    }
                    Customer.arrayCustomers.sortBy { list -> list.name }
                    customerIdForEdit = ""
                    newCustomerListener!!.onNewCustomerListener()
                } else {
                    var uudi = UUID.randomUUID()
                    var customerExpanded = CustomerExpanded("", "", "", "", "", "")
                    var newCustomer = Customer(
                        uudi.toString(),
                        editName!!.text.toString(),
                        editPrename!!.text.toString(),
                        editStreetName!!.text.toString(),
                        editStreetNumber!!.text.toString(),
                        editPlz!!.text.toString(),
                        editLocation!!.text.toString(),
                        editProjectNumber!!.text.toString(),
                        customerExpanded
                    )
                    Customer.arrayCustomers.add(newCustomer)
                    Customer.arrayCustomers.sortBy { list -> list.name }
                    newCustomerListener!!.onNewCustomerListener()

                }
                this.dismiss()
            } else {
                Toast.makeText(requireContext(), "Bitte Nachname oder Vorname hinzufügen", Toast.LENGTH_SHORT).show()
            }




        }
        buttonCancel!!.setOnClickListener {
            this.dismiss()
        }
        buttonCreateNewCustomer!!.setOnClickListener {

            for (customer in Customer.arrayCustomers){
                if (customer.name == editName!!.text.toString() && customer.preName == editPrename!!.text.toString() && customer.streetName == editStreetName!!.text.toString() && customer.streetNumber == editStreetNumber!!.text.toString()){
                    Toast.makeText(requireContext(), "Kunde existiert bereits", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

            }
            if (!editName!!.text.toString().isEmpty() && !editPrename!!.text.toString().isEmpty()) {

                var uudi = UUID.randomUUID()
                var customerExpanded = CustomerExpanded("", "", "", "", "", "")
                var newCustomer = Customer(
                    uudi.toString(),
                    editName!!.text.toString(),
                    editPrename!!.text.toString(),
                    editStreetName!!.text.toString(),
                    editStreetNumber!!.text.toString(),
                    editPlz!!.text.toString(),
                    editLocation!!.text.toString(),
                    editProjectNumber!!.text.toString(),
                    customerExpanded
                )
                Customer.arrayCustomers.add(newCustomer)
                Customer.arrayCustomers.sortBy { list -> list.name }
                newCustomerListener!!.onNewCustomerListener()

                Toast.makeText(requireContext(), "Kunde hinzugefügt", Toast.LENGTH_SHORT).show()
                setSpinnerCustomerContent()

            } else {
                Toast.makeText(requireContext(), "Bitte Nachname oder Vorname hinzufügen", Toast.LENGTH_SHORT).show()
            }
        }
    }



    companion object {
        fun newInstance(title: String?): CustomerFragment {
            val frag = CustomerFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }

    override fun onClientEventlistener(customerID: String) {
        customerIdForEdit = customerID
    }


}