package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.example.myapplication.Objects.Customer
import com.example.myapplication.Objects.CustomerExpanded

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

            customerID = getArguments()?.getString("customerid").toString();
            return inflater.inflate(R.layout.customer_client_fragment, container)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view

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

        val title = requireArguments().getString("title", "Enter Name")
        dialog!!.setTitle(title)
        getDialog()?.getWindow()?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
        setContent()
        onButtonClickListeners()
    }

    private fun setContent() {
      for (client in Customer.arrayCustomers){
          if (client.customerId == customerID){
              if (client.customerExpanded.clientName != ""){
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

    private fun onButtonClickListeners() {
      buttonSaveClient!!.setOnClickListener {
          for (customer in Customer.arrayCustomers){
              if (customer.customerId == customerID){

                  var clientCust = CustomerExpanded("","","","","","")
                  clientCust.clientName = editName!!.text.toString()
                  clientCust.clientPreName = editPrename!!.text.toString()
                  clientCust.clientStreetName = editStreetName!!.text.toString()
                  clientCust.clientStreetNumber = editStreetNumber!!.text.toString()
                  clientCust.clientPlz = editPlz!!.text.toString()
                  clientCust.clientLocation = editLocation!!.text.toString()
                  customer.customerExpanded = clientCust
                  var xmlTool = XmlTool()
                  xmlTool.saveProfilesToXml(Customer.arrayCustomers,requireContext())
                  this.dismiss()
              }
          }
          newClientListener!!.onClientEventlistener(customerID)
          this.dismiss()
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
            frag.setArguments(args)
            return frag
        }
    }

}