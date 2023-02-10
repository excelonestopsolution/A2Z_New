package com.di_md.a2z.util.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Button
import android.widget.TextView
import com.di_md.a2z.R
import com.di_md.a2z.util.dialogConfiguration
import com.di_md.a2z.util.dialogFullScreenConfiguration
import com.di_md.a2z.util.ents.hide
import com.di_md.a2z.util.ents.set
import com.di_md.a2z.util.ents.setupTextColor
import com.di_md.a2z.util.ents.show
import com.di_md.a2z.util.enums.LottieType
import com.airbnb.lottie.LottieAnimationView


object StatusDialog {

    fun showRetryMessage(
        context: Context?, message: String?,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        val dialog: AlertDialog.Builder = Builder(context)
        dialog.setTitle("A2Z Suvidhaa")
        dialog.setMessage(message)
        dialog.setPositiveButton("Try Again", onClickListener)
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }

    fun showErrorMessage(
        context: Context?, message: String?,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        val dialog: AlertDialog.Builder = Builder(context)
        dialog.setTitle("A2Z Suvidhaa")
        dialog.setMessage(message)
        dialog.setPositiveButton("OK", onClickListener)
        dialog.show()
    }

    fun progress(context: Context, title: String = "Loading..."): Dialog {
        return dialogConfiguration(context, R.layout.dialog_loading,cancelable = false).apply {
            findViewById<TextView>(R.id.tv_title).text = title
            show()
        }
    }

    fun fullScreenProgress(context: Context
    ): Dialog {
        return dialogFullScreenConfiguration(context, R.layout.dialog_full_screen_progress, cancelable = false).apply { show() }
    }


    fun success(
        activity: Activity,
        message: String = "Success",
        title: String? = null,
        onComplete: () -> Unit = {},
    ) = statusDialog(activity, message, onComplete, StatusType.SUCCESS,title)


    fun failure(
        activity: Activity,
        message: String = "Failure",
        onComplete: () -> Unit = {}
    ) = statusDialog(activity, message, onComplete, StatusType.FAILURE)

    fun pending(
        activity: Activity,
        message: String = "Pending"
    ) = statusDialog(activity, message, StatusType.PENDING)


    fun alert(
            activity: Activity,
            message: String,
            onComplete: () -> Unit = {},
    ) = statusDialog(activity, message,onComplete, StatusType.ALERT)



    fun success(
            activity: Activity,
            message: String = "Success"
    ) = statusDialog(activity, message, StatusType.SUCCESS)


    fun failure(
            activity: Activity,
            message: String = "Failure"
    ) = statusDialog(activity, message, StatusType.FAILURE)

    fun pending(
            activity: Activity,
            message: String = "Pending",
            onComplete: () -> Unit = {}
    ) = statusDialog(activity, message, onComplete, StatusType.PENDING)


    private fun statusDialog(
            context: Context,
            message: String,
            type: StatusType,

    ) = dialogConfiguration(
            context = context,
            layout = R.layout.dialog_status
    ).apply {

        val tvMessage = findViewById<TextView>(R.id.tv_message).also { it.text = message }

        val lottieView = findViewById<LottieAnimationView>(R.id.lottie_view)

        when (type) {

            StatusType.SUCCESS -> {
                lottieView.set(LottieType.SUCCESS)
                tvMessage.setupTextColor(R.color.green)
            }
            StatusType.FAILURE -> {
                lottieView.set(LottieType.FAILURE)
                tvMessage.setupTextColor(R.color.red)
            }
            StatusType.PENDING -> {
                lottieView.set(LottieType.PENDING)
                tvMessage.setupTextColor(R.color.yellow_dark)
            }
            StatusType.ALERT ->{
                lottieView.set(LottieType.ALERT)
                tvMessage.setupTextColor(R.color.colorPrimaryDark)
            }
        }

        findViewById<Button>(R.id.btn_ok).setOnClickListener { dismiss() }
        show()
    }


    private fun statusDialog(
        context: Context,
        message: String,
        onComplete: () -> Unit,
        type: StatusType,
        title : String? = null,
    ) = dialogConfiguration(
        context = context,
        layout = R.layout.dialog_status
    ).apply {

        val tvMessage = findViewById<TextView>(R.id.tv_message).also { it.text = message }
        val lottieView = findViewById<LottieAnimationView>(R.id.lottie_view)
        val tvTitle = findViewById<TextView>(R.id.tv_title).also { it.text = title }
        if(title == null) tvTitle.hide() else tvTitle.show()

        when (type) {

            StatusType.SUCCESS -> {
                lottieView.set(LottieType.SUCCESS)
                tvMessage.setupTextColor(R.color.green)
            }
            StatusType.FAILURE -> {
                lottieView.set(LottieType.FAILURE)
                tvMessage.setupTextColor(R.color.red)
            }
            StatusType.PENDING -> {
                lottieView.set(LottieType.PENDING)
                tvMessage.setupTextColor(R.color.yellow_dark)
            }
            StatusType.ALERT -> {
                lottieView.set(LottieType.ALERT)
                tvMessage.setupTextColor(R.color.grey)
            }
        }

        findViewById<Button>(R.id.btn_ok).setOnClickListener { dismiss() }
        setOnDismissListener { onComplete() }
        show()
    }

    enum class StatusType {
        SUCCESS, FAILURE, PENDING,ALERT
    }
}