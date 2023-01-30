package com.a2z.app.service.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDatabase {

    private val db = Firebase.firestore

    companion object{
        const val LOG_COLLECTION= "app_log"
    }

    fun insertLog(log : FBAppLog){
        db.collection(LOG_COLLECTION)
            .add(log)
    }

}