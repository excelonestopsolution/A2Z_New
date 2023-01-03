package com.a2z.app.util

import android.content.Intent

object AepsUtil {

    const val PIP_OPTION  = """<PidOptions ver="1.0">
       <Opts env="P" fCount="1" fType="2" format="0" iCount="0" iType="0" pCount="0" pType="0" pidVer="2.0" posh="UNKNOWN" timeout="10000"/>
    </PidOptions>"""

    fun pidIntent(packageName : String) : Intent {
        val intent = Intent()
        intent.setPackage(packageName)
        intent.action = "in.gov.uidai.rdservice.fp.CAPTURE"
        intent.putExtra("PID_OPTIONS", PIP_OPTION)
        return intent
    }

}