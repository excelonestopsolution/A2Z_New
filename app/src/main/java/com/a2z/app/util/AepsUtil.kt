package com.a2z.app.util

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.a2z.app.data.model.auth.AepsDriver
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

object AepsUtil {

    var PIP_OPTION = ""
    var action = ""

    fun pidIntent(aepsDriver: AepsDriver): Intent {

        if (aepsDriver.driver_name == "Iris"){
            PIP_OPTION = """<PidOptions ver="1.0">
       <Opts env="P" format="0" iCount="1" iType="0" pidVer="2.0" timeout="10000"/>
    </PidOptions>"""
            action = "in.gov.uidai.rdservice.iris.CAPTURE"
        }
        else{
            PIP_OPTION = """<PidOptions ver="1.0">
       <Opts env="P" fCount="1" fType="2" format="0" iCount="0" iType="0" pCount="0" pType="0" pidVer="2.0" posh="UNKNOWN" timeout="10000"/>
    </PidOptions>"""
            action = "in.gov.uidai.rdservice.fp.CAPTURE"
        }
        val intent = Intent()
        intent.setPackage(aepsDriver.package_name)
        intent.action = action
        intent.putExtra("PID_OPTIONS", PIP_OPTION)
        return intent
    }


    fun biometricResult(it: ActivityResult): Pair<String?, String?> {
        return if (it.resultCode == Activity.RESULT_OK || it.data != null) {
            try {
                val result = it.data!!.getStringExtra("PID_DATA")
                if (result != null) {
                    val respString: Array<String> = parse(result)
                    if (respString[0].equals("0", ignoreCase = true)) Pair(result, null)
                    else Pair(null, "Biometric result is null, please try again")
                } else Pair(null, "Biometric result is null, please try again")

            } catch (e: Exception) {
                Pair(null, "please check biometric device is connected")
            }
        } else Pair(null, "please check biometric device is connected")
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun parse(xml: String?): Array<String> {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val xmlPullParser = factory.newPullParser()
        xmlPullParser.setInput(StringReader(xml))
        val respStrings = arrayOf("na", "na")
        var eventType = xmlPullParser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                println("Start document")
            } else if (eventType == XmlPullParser.START_TAG) {
                if (xmlPullParser.name.equals("Resp", ignoreCase = true)) {
                    val count = xmlPullParser.attributeCount
                    for (i in 0 until count) {
                        val attributeName = xmlPullParser.getAttributeName(i)
                        println(attributeName)
                        if (attributeName.equals("errCode", ignoreCase = true)) {
                            respStrings[0] = xmlPullParser.getAttributeValue(i)
                            println("errCode : " + xmlPullParser.getAttributeValue(i))
                        }
                        if (attributeName.equals("errInfo", ignoreCase = true)) {
                            respStrings[1] = xmlPullParser.getAttributeValue(i)
                            println("errInfo : " + xmlPullParser.getAttributeValue(i))
                        }
                    }
                }
            }
            eventType = xmlPullParser.next()
        }
        return respStrings
    }
}