package com.di_md.a2z.fragment.report

import android.app.Dialog
import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.di_md.a2z.R
import com.di_md.a2z.data.model.report.ComplainType
import com.di_md.a2z.util.dialogConfiguration
import com.di_md.a2z.util.ents.setup
import com.google.android.material.textfield.TextInputLayout

object LedgerReportComponent {


    operator fun invoke(
        context: Context,
        complainTypes: List<ComplainType>?,
        onSubmit: (ComplainType, String) -> Unit
    ): Dialog {

        val names = complainTypes?.map { it.name }


        var complainType :  ComplainType? = null

        return dialogConfiguration(context, R.layout.dialog_report_complain_2).apply {

            val actvReason = findViewById<AutoCompleteTextView>(R.id.actv_reason)
            val tilReason = findViewById<TextInputLayout>(R.id.til_actv_reason)
            val edRemark = findViewById<EditText>(R.id.ed_remark)

            actvReason.setup(names as ArrayList<String>) {name->
                complainType = complainTypes.find { it.name ==  name}
            }

            findViewById<ImageButton>(R.id.btn_cancel).setOnClickListener {
                dismiss()
            }

            findViewById<Button>(R.id.btn_proceed).setOnClickListener {
                if (complainType != null) {
                    tilReason.isErrorEnabled = false
                    val remark = edRemark.text.toString()
                    onSubmit(complainType!!, remark)
                    dismiss()
                } else {
                    tilReason.error = "Select reason for complain"
                    tilReason.isErrorEnabled = true
                }
            }
            show()
        }
    }


}