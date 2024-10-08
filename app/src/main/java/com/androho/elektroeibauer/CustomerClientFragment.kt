package com.androho.elektroeibauer

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.androho.elektroeibauer.Objects.Customer
import com.androho.elektroeibauer.Objects.CustomerExpanded

class CustomerClientFragment : DialogFragment() {



    var editPrename: EditText? = null
    var editName: EditText? = null
    var editStreetName: EditText? = null
    var editStreetNumber: EditText? = null
    var editPlz: EditText? = null
    var editLocation: EditText? = null
    var buttonCancel: Button? = null
    var buttonClearText: Button? = null
    var buttonSaveClient : Button? = null
    var customerID : String= ""

    var buttonDeleteExmpanded : Button? = null
    interface onClientEventListener {
        fun onClientEventlistener(customerID :String)
    }

    var newClientListener: onClientEventListener? = null
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            newClientListener = activity as onClientEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

            customerID = arguments?.getString("customerid").toString()




            return inflater.inflate(R.layout.customer_client_fragment, container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var windowManager : WindowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var display = windowManager.defaultDisplay
        var lp : FrameLayout.LayoutParams = FrameLayout.LayoutParams(display.width,display.height)
        view.layoutParams = lp
        // Fetch arguments from bundle and set title
        editLocation = view.findViewById(R.id.editTextLocationCustomerClient)
        editName = view.findViewById(R.id.editTextNachnameCustomerClient)
        editPlz = view.findViewById(R.id.editTextPlzCustomerClient)
        editPrename = view.findViewById(R.id.editTextVornameCustomerClient)
        editStreetName = view.findViewById(R.id.editTextStreetCustomerClient)
        editStreetNumber = view.findViewById(R.id.editTextStreetNumberCustomerClient)
        buttonCancel = view.findViewById(R.id.buttonCancelClient)
        buttonClearText = view.findViewById(R.id.buttonClearClient)
        buttonSaveClient = view.findViewById(R.id.buttonSaveClient)
        buttonDeleteExmpanded = view.findViewById(R.id.buttonDeleteClientFragmentExpanded)

        val title = requireArguments().getString("title", "Enter Name")
        dialog!!.setTitle(title)
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
        setContent()
        onButtonClickListeners()
    }

    private fun setContent() {
      for (client in Customer.arrayCustomers){
          if (client.customerId == customerID){
              if(client.customerExpanded != null) {
                  if (client.customerExpanded.clientName != "") {
                      editName!!.setText(client.customerExpanded.clientName)
                      editPrename!!.setText(client.customerExpanded.clientPreName)
                      editStreetName!!.setText(client.customerExpanded.clientStreetName)
                      editStreetNumber!!.setText(client.customerExpanded.clientStreetNumber)
                      editPlz!!.setText(client.customerExpanded.clientPlz)
                      editLocation!!.setText(client.customerExpanded.clientLocation)
                  }
              }
          }
      }
    }



    private fun onButtonClickListeners() {
      buttonSaveClient!!.setOnClickListener {
          if(!editName!!.text.toString().isEmpty() && !editPrename!!.text.toString().isEmpty()) {
              for (customer in Customer.arrayCustomers) {
                  if (customer.customerId == customerID) {

                      var clientCust = CustomerExpanded("", "", "", "", "", "")
                      clientCust.clientName = editName!!.text.toString()
                      clientCust.clientPreName = editPrename!!.text.toString()
                      clientCust.clientStreetName = editStreetName!!.text.toString()
                      clientCust.clientStreetNumber = editStreetNumber!!.text.toString()
                      clientCust.clientPlz = editPlz!!.text.toString()
                      clientCust.clientLocation = editLocation!!.text.toString()
                      customer.customerExpanded = clientCust
                      var xmlTool = XmlTool()
                      xmlTool.saveProfilesToXml(Customer.arrayCustomers, requireContext())
                      this.dismiss()
                  }
              }
              newClientListener!!.onClientEventlistener(customerID)
              this.dismiss()
          }
          else{
              Toast.makeText(requireContext(), "Bitte Nachname oder Vorname hinzufügen", Toast.LENGTH_SHORT).show()
          }
      }

        buttonDeleteExmpanded!!.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Kunde löschen")
            builder.setMessage("Wollen Sie wirklich den Kunden löschen?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                for (customer in Customer.arrayCustomers) {
                    if (customer.customerId == customerID) {

                        var clientCust = CustomerExpanded("", "", "", "", "", "")
                        customer.customerExpanded = clientCust
                        var xmlTool = XmlTool()
                        xmlTool.saveProfilesToXml(Customer.arrayCustomers, requireContext())
                        this.dismiss()
                    }
                }
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }

        buttonCancel!!.setOnClickListener {
            this.dismiss()
        }

        buttonClearText!!.setOnClickListener {
            editLocation!!.setText("")
            editName!!.setText("")
            editPrename!!.setText("")
            editPlz!!.setText("")
            editStreetName!!.setText("")
            editStreetNumber!!.setText("")
        }
    }

    companion object {
        fun newInstance(title: String?): CustomerClientFragment {
            val frag = CustomerClientFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }

}