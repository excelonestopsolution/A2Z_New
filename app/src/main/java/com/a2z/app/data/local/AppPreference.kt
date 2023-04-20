package com.a2z.app.data.local

import android.content.Context
import com.a2z.app.data.model.auth.AepsDriver
import com.a2z.app.data.model.auth.User
import com.a2z.app.data.model.dmt.UpiStaticMessage
import com.a2z.app.data.model.indonepal.INStaticData
import javax.inject.Inject

class AppPreference @Inject constructor(context: Context) : BasePreference(context) {

    var appUniqueKey: String
        set(value) = saveString(UNIQUE_KEY, value)
        get() = getString(UNIQUE_KEY)

    var user: User?
        set(value) = saveObject(USER, value)
        get() = getObject(USER, User::class.java)
    var upiStateMessage: UpiStaticMessage?
        set(value) = saveObject(UPI_STATIC_MESSAGE, value)
        get() = getObject(UPI_STATIC_MESSAGE, UpiStaticMessage::class.java)

    var password: String
        set(value) = saveString(PASSWORD, value)
        get() = getString(PASSWORD)

    var loginId: String
        set(value) = saveString(LOGIN_ID, value)
        get() = getString(LOGIN_ID)

    var loginCheck: Boolean
        set(value) = saveBoolean(LOGIN_CHECK, value)
        get() = getBoolean(LOGIN_CHECK)

    var latitude: String
        set(value) = saveString(LATITUDE, value)
        get() = getString(LATITUDE)
    var longitude: String
        set(value) = saveString(LONGITUDE, value)
        get() = getString(LONGITUDE)

    var locationFetched: Boolean
        set(value) = saveBoolean(LOCATION_FETCHED, value)
        get() = getBoolean(LOCATION_FETCHED)


}

