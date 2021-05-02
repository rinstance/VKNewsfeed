package com.example.vknewsfeed.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.vknewsfeed.R
import kotlinx.android.synthetic.main.dialog_fragment_new_post.*
import kotlinx.android.synthetic.main.dialog_fragment_new_post.view.*

class NewPostDialogFragment : DialogFragment() {
    private lateinit var clickListeners: Listeners
    private lateinit var imageActionButton: Button
    private lateinit var imageViewing: ImageView
    private lateinit var messEdiText: EditText

    interface Listeners {
        fun create(message: String)
        fun attachImage(isSelected: Boolean)
    }

    fun setClickListeners(clickListeners: Listeners) {
        this.clickListeners = clickListeners
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_fragment_new_post, null)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageActionButton.setOnClickListener { clickListeners.attachImage(imageViewing.visibility != View.GONE) }
        text_cancel_button.setOnClickListener { dismiss() }
        text_create_button.setOnClickListener { clickListeners.create(messEdiText.text.toString()) }
    }

    private fun initView(view: View) {
        imageActionButton = view.button_image_action
        imageViewing = view.image_viewing
        messEdiText = view.name_edit_text
    }

    fun setImage(imageUri: Uri?) {
        imageUri?.let {
            imageViewing.visibility = View.VISIBLE
            imageViewing.setImageURI(it)
            imageActionButton.setText(R.string.delete_image)
        }
    }

    fun deleteImage() {
        imageViewing.visibility = View.GONE
        imageActionButton.setText(R.string.attach_image)
    }
}