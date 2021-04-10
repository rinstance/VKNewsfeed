package com.example.vknewsfeed.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.vknewsfeed.R
import kotlinx.android.synthetic.main.dialog_fragment_info.*

class InfoDialogFragment : DialogFragment() {
    private lateinit var cancelButton: TextView
    private lateinit var okButton: TextView
    private var message: String? = null
    private  var okText: String? = null
    private lateinit var listener: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_fragment_info, null)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_message.text = message
        if (okText != null)
            okButton.text = okText
        okButton.setOnClickListener {
            listener
            dismiss()
        }
        cancelButton.setOnClickListener { dismiss() }
    }

    fun setOKListener(listener: () -> Unit) {
        this.listener = listener
    }

    private fun initView(view: View) {
        cancelButton = view.findViewById(R.id.button_cancel)
        okButton = view.findViewById(R.id.button_ok)
    }

    fun setMessage(text: String) {
        message = text
    }

    fun setOkText(text: String) {
        okText = text
    }
}