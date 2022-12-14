package com.a2z.app.data.local

import android.content.Context
import com.a2z.app.data.model.auth.User
import javax.inject.Inject

class AppPreference @Inject constructor(context: Context) : BasePreference(context) {

    var appUniqueKey: String
        set(value) = saveString(UNIQUE_KEY, value)
        get() = getString(UNIQUE_KEY)

    var user: User?
        set(value) = saveObject(USER, value)
        get() = getObject(USER, User::class.java)

    var password: String
        set(value) = saveString(PASSWORD, value)
        get() = getString(PASSWORD)

    var loginId: String
        set(value) = saveString(LOGIN_ID, value)
        get() = getString(LOGIN_ID)

    var loginCheck : Boolean
        set(value) = saveBoolean(LOGIN_CHECK, value)
        get() = getBoolean(LOGIN_CHECK)




}

