package com.mtr.kisanbazaar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mtr.kisanbazaar.R
import com.mtr.kisanbazaar.daos.PostDao
import com.mtr.kisanbazaar.models.Post
import com.raghib.linkapp.utils.TimeUtils


class PostAdapter(options: FirestoreRecyclerOptions<Post>) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options
) {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val userName: TextView=itemView.findViewById(R.id.userName)
        val imgUser:ImageView=itemView.findViewById(R.id.imgUser)
        val postTime:TextView=itemView.findViewById(R.id.postTime)
        val tvPost:TextView=itemView.findViewById(R.id.tvPost)
        val imgLike:ImageView=itemView.findViewById(R.id.imgLikeButton)
        val likeCount:TextView=itemView.findViewById(R.id.tvLikeCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val viewHolder=  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_home_page,parent,false))

        viewHolder.imgLike.setOnClickListener {

            if(PostDao().addLiked(snapshots.getSnapshot(viewHolder.adapterPosition).id))
            {
                viewHolder.imgLike.setImageResource(R.drawable.ic_liked)
            }
            else
                viewHolder.imgLike.setImageResource(R.drawable.ic_not_liked)
        }
        return  viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.userName.text=model.user.displayName
        holder.postTime.text= TimeUtils().getTimeAgo(model.createdAt)
        holder.likeCount.text=model.likedBy.size.toString()
        holder.tvPost.text=model.text
        Glide.with(holder.imgUser.context).load(model.user.imgUrl).circleCrop().into(holder.imgUser)

        if(model.likedBy.contains(Firebase.auth.currentUser!!.uid))
         holder.imgLike.setImageResource(R.drawable.ic_liked)
        else
            holder.imgLike.setImageResource(R.drawable.ic_not_liked)


//
//        val auth=Firebase.auth
//        holder.imgLike.setOnClickListener {
//            if(model.likedBy.contains(auth.currentUser!!.uid))
//            {
//                model.likedBy.remove(auth.currentUser!!.uid)
//            }
//            else
//            { model.likedBy.add(auth.currentUser!!.uid)
//
//            }
//            PostDao().addLiked()
//
//        }


    }


}