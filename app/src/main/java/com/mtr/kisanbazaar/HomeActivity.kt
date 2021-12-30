package com.mtr.kisanbazaar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.Query
import com.mtr.kisanbazaar.adapters.PostAdapter
import com.mtr.kisanbazaar.daos.PostDao
import com.mtr.kisanbazaar.models.Post


lateinit var rvHome: RecyclerView
class HomeActivity : AppCompatActivity() {

    val postDao: PostDao = PostDao()
    lateinit var postAdapter: PostAdapter
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvHome =findViewById(R.id.rvHome)
        bottomNavigationView=findViewById(R.id.bottomNavigationView)




        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    //setContentView(R.layout.activity_main)
                    supportActionBar!!.title="Home"

                    true
                }
                R.id.item_post -> {

                    val intent = Intent(this@HomeActivity, NewPost::class.java)
                    startActivity(intent)

                    true
                }
                R.id.item_trending -> {

                    Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show()
                    supportActionBar!!.title="Trending"
                    true
                }
                R.id.item_notification -> {

                    Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show()
                    supportActionBar!!.title="Notification"
                    true
                }

                R.id.item_profile -> {

                    Toast.makeText(this, "coming soon..", Toast.LENGTH_SHORT).show()
                    supportActionBar!!.title ="My Profile"
                    true
                }
                else -> false
            }
        }




        val query=postDao.postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val rvOptions=FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()

        postAdapter=PostAdapter(rvOptions)
        rvHome.layoutManager=LinearLayoutManager(this)
        rvHome.adapter=postAdapter


        scrollActions()            //detects user scroll activities on Home RV

    }

    private fun scrollActions() {
        rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //reached top
                    Log.d("scroll", ": reached top")
                    bottomNavigationView.visibility= View.VISIBLE
                    //supportActionBar!!.show()
                }
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("scroll", " : reached end")
                    bottomNavigationView.visibility= View.VISIBLE
                }
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    Log.d("scroll", " :scrolling ")
                    bottomNavigationView.visibility= View.GONE
                    supportActionBar!!.hide()
                }

            }
        })


    }

    override fun onStart() {
        super.onStart()
        postAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        postAdapter.stopListening()
    }
}