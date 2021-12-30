package com.mtr.kisanbazaar.daos

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mtr.kisanbazaar.models.Post
import com.mtr.kisanbazaar.models.User


class PostDao {

    val  db=FirebaseFirestore.getInstance()
    val postCollection=db.collection("posts")
    val auth=Firebase.auth

   suspend fun addPost(text:String)
    {       val currentUser = auth.currentUser


            val post = Post()
                post.text = text
                post.createdAt=System.currentTimeMillis()


        if (currentUser != null) {
            post.user = User(currentUser.uid,currentUser?.displayName,currentUser?.photoUrl.toString())
        }
             postCollection.add(post)



    }

    fun addLiked(postId:String): Boolean
    {
        val currentUser = auth.currentUser
        var flag=false
     val postTask=postCollection.document(postId).get()

         postTask.addOnSuccessListener {
             //Log.d("like", "addLiked:  $it }")
            // Log.d("like", "addLiked: ${it.get("likedBy")}")
             val post=it.toObject(Post::class.java)
             if(post!!.likedBy.contains(currentUser!!.uid))
             {
                 post.likedBy.remove(currentUser.uid)
                 flag=false
             }
             else {
                 post.likedBy.add(currentUser.uid)
                 flag=true
             }

             postCollection.document(postId).update("likedBy",post.likedBy)
         }

       return  flag
    }





}