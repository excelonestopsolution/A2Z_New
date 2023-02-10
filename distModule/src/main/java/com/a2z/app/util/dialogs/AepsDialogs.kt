package com.a2z.app.util.dialogs

import android.content.Context
import android.widget.*
import com.a2z.app.AppPreference
import com.a2z.app.R
import com.a2z.app.util.dialogConfiguration
import com.a2z.app.util.ents.setupAdapter

object AepsDialogs {



    fun kycRequired(
        context: Context,
        title: String,
        description: String,
        onComplete: () -> Unit = {},
    ) = dialogConfiguration(context, R.layout.dialog_kyc_alert).apply {
        findViewById<Button>(R.id.btn_proceed).setOnClickListener {
            dismiss()
            onComplete()
        }
        findViewById<ImageButton>(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }

        findViewById<TextView>(R.id.tv_title).text = title
        findViewById<TextView>(R.id.tv_description).text = description

        show()
    }

    fun aadhaarKycConfirm(
        context: Context,
        aadhaarNumber: String,
        mobileNumber: String
    ) = dialogConfiguration(context, R.layout.dialog_aadhaar_kyc_confirm).apply {

        findViewById<TextView>(R.id.tv_aadhaar_number).text = aadhaarNumber
        findViewById<TextView>(R.id.tv_mobile_number).text = mobileNumber

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }
        show()
    }

    inline fun selectRDDevice(
        context: Context,
        type: String,
        selectedDevice: String,
        crossinline onApprove: (device: String, packageUrl: String) -> Unit
    ) = dialogConfiguration(context, R.layout.dialog_rd_device).apply {

        var mSelectedDevice = selectedDevice

        val mantraPackage = "com.mantra.rdservice"
        val morphoPackage = "com.scl.rdservice"
        val startekPackage = "com.acpl.registersdk"

        var selectRDPackage = mantraPackage
        val deviceList = arrayOf("MANTRA", "MORPHO", "STARTEK")


        val dataAdapter = ArrayAdapter(
            context,
            R.layout.spinner_layout, deviceList
        )
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout)

        val spinner = findViewById<Spinner>(R.id.spn_selectDevice)
        spinner.adapter = dataAdapter
        spinner.setupAdapter(deviceList, onItemSelected = {
            AppPreference.getInstance(context).setSelectRdServiceDevice(it)
            mSelectedDevice = it
            selectRDPackage = when (it) {
                "MANTRA" -> mantraPackage
                "MORPHO" -> morphoPackage
                "STARTEK" -> startekPackage
                else -> mantraPackage
            }
        })

        spinner.setSelection(deviceList.indexOf(selectedDevice))


        findViewById<TextView>(R.id.tv_message).text =
            "Please connect biometric device and complete $type"
        findViewById<Button>(R.id.btn_proceed).setOnClickListener {
            onApprove(mSelectedDevice, selectRDPackage)
            dismiss()
        }
        show()
    }


}