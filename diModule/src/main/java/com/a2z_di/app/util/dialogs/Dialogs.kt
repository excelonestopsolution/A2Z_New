package com.a2z_di.app.util.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.text.InputFilter
import android.widget.*
import com.a2z_di.app.R
import com.a2z_di.app.util.ViewUtil
import com.a2z_di.app.util.dialogConfiguration
import com.a2z_di.app.util.dialogFullScreenConfiguration
import com.a2z_di.app.util.ents.hide
import com.a2z_di.app.util.ents.setupPicassoImage
import com.google.android.material.textfield.TextInputLayout


object Dialogs {



    inline fun imageAndCameraChooser(
        context: Context,
        crossinline onGallery: () -> Unit,
        crossinline onCamera: () -> Unit
    ) {
        AlertDialog.Builder(context).apply {
            setTitle("Choose Camera or Gallery")
            setIcon(R.drawable.ic_baseline_file_copy_24)
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            setItems(arrayOf("Gallery", "Camera")) { _, which ->
                when (which) {
                    0 -> onGallery()
                    1 -> onCamera()

                }
            }
            show()
        }
    }


    fun confirmField(context: Context, input: String, forInput: String = "Number"): Dialog {
        return dialogConfiguration(context, R.layout.dialog_confirm).apply {
            findViewById<TextView>(R.id.tv_message).text = input
            findViewById<TextView>(R.id.tv_message_1).text = "Please confirm provided $forInput"
            findViewById<TextView>(R.id.tv_message_2).text =
                "$forInput once submitted can not be change"
            findViewById<Button>(R.id.btn_cancel).setOnClickListener { dismiss() }
            show()
        }
    }



    fun otpVerify(
        context: Context,
        showTimer: Boolean = true,
        otpLength: Int = 4,
        onSubmit: (String) -> Unit = {}
    ): Dialog {
        if (!showTimer) {
            return dialogConfiguration(context, R.layout.dialog_otp_verify).apply {
                findViewById<RelativeLayout>(R.id.rv_count).hide()
                findViewById<TextView>(R.id.tv_waiting_hint).hide()


                val tilOtp = findViewById<TextInputLayout>(R.id.til_otp)
                val edOtp = findViewById<EditText>(R.id.ed_otp)
                edOtp.filters = arrayOf(InputFilter.LengthFilter(otpLength))

                ViewUtil.resetErrorOnTextInputLayout(tilOtp)

                findViewById<Button>(R.id.btn_verify).setOnClickListener {
                    val otp = edOtp.text.toString()
                    if (otp.length == otpLength) {
                        tilOtp.isErrorEnabled = false
                        dismiss()
                        onSubmit(otp)

                    } else {
                        tilOtp.error = "Enter $otpLength digits otp"
                        tilOtp.isErrorEnabled = true

                    }
                }

                findViewById<ImageButton>(R.id.btn_cancel).setOnClickListener {
                    dismiss()
                }
                show()
            }
        } else return dialogConfiguration(context, R.layout.dialog_otp_verify).apply {
            show()
        }
    }

    fun dmtCommissionAmount(context: Context): Dialog {
        return dialogConfiguration(context, R.layout.dialog_dmt_comission_amount).apply {
            show()
        }
    }


    fun commonConfirmDialog(
        context: Context,
        message: String
    ): Dialog {
        return dialogConfiguration(context, R.layout.dialog_common_confirm).apply {

            findViewById<TextView>(R.id.tv_message).text = message

            findViewById<ImageButton>(R.id.btn_cancel).setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    fun createLoginID(
        context: Context,
        onProceed: () -> Unit
    ): Dialog {
        return dialogConfiguration(context, R.layout.dialog_login_id).apply {
            findViewById<ImageButton>(R.id.btn_close).setOnClickListener { dismiss() }
            findViewById<Button>(R.id.btn_create_login_id).setOnClickListener {
                dismiss()
                onProceed()
            }
            show()
        }
    }


    fun docImageView(
        context: Context,
        strImage: String
    ): Dialog {
        return dialogFullScreenConfiguration(context, R.layout.dialog_doc_image).apply {

            val progress = findViewById<ProgressBar>(R.id.progressBar1)
            val imageView = findViewById<ImageView>(R.id.image_view)

            findViewById<Button>(R.id.btn_back).setOnClickListener { dismiss() }
            imageView.setupPicassoImage(strImage) {
                progress.hide()
            }
            show()
        }
    }

    fun docImageView(
        context: Context,
        bitmap: Bitmap,
    ): Dialog {
        return dialogFullScreenConfiguration(context, R.layout.dialog_doc_image).apply {

            val progress = findViewById<ProgressBar>(R.id.progressBar1)
            progress.hide()
            val imageView = findViewById<ImageView>(R.id.image_view)

            findViewById<Button>(R.id.btn_back).setOnClickListener { dismiss() }
            imageView.setImageBitmap(bitmap)
            show()
        }
    }



}