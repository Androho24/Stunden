package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.github.gcacace.signaturepad.views.SignaturePad

class SigningFragment : DialogFragment() {

    var buttonClear : Button? = null
    var buttonComplete : Button? = null
    var imageSigned : ImageView? = null
    var signaturePadWorker : SignaturePad? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        return inflater.inflate(R.layout.signing_worker_fragment, container)
    }

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
buttonClear = view.findViewById(R.id.buttonClearWorkerSigning)
        buttonComplete = view.findViewById(R.id.buttonCompleteWorkerSigning)
        signaturePadWorker = view.findViewById(R.id.signaturePad)
        imageSigned = ImageView(requireContext())
        val title = requireArguments().getString("title", "Enter Name")
        dialog!!.setTitle(title)
        setButtonOnClickListeners()

        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
    }

    interface onSignedCompleteListener {
        fun onSignedCompleteListener(imageView: ImageView)
    }
    var signedCompleteListener: onSignedCompleteListener? = null
    private fun setButtonOnClickListeners() {
        buttonComplete!!.setOnClickListener {
            imageSigned!!.setImageBitmap(signaturePadWorker!!.signatureBitmap)
           signedCompleteListener!!.onSignedCompleteListener(imageSigned!!)
            this.dismiss()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            signedCompleteListener = activity as onSignedCompleteListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    companion object {
        fun newInstance(title: String?): SigningFragment {
            val frag = SigningFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }
}