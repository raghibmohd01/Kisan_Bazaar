package com.mtr.kisanbazaar.daos

import com.google.firebase.firestore.FirebaseFirestore
import com.mtr.kisanbazaar.models.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    val db= FirebaseFirestore.getInstance()
    val userCollection=db.collection("users")

    fun addUser(user: User?)
    {
        GlobalScope.launch(Dispatchers.IO) {
            user?.let {
                userCollection.document(user.uid).set(it)
            }
        }


    }

//    suspend fun postPast(post:Post): Task<DocumentReference> = userCollection.add(post)
//
//    suspend fun getUser(uid:String) : Task<DocumentSnapshot> = userCollection.document(uid).get()


}