package com.example.northamptontravels.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.northamptontravels.R
import com.example.northamptontravels.activity.ProfileActivity
import com.example.northamptontravels.activity.ReplyActivity
import com.example.northamptontravels.activity.ReviewActivity
import com.example.northamptontravels.adapter.ReviewAdapter.ViewHolder
import com.example.northamptontravels.entity.Review

class ReviewAdapter (private val reviewDataset:ArrayList<Review>,
                     private val isShowAll: Boolean) : RecyclerView.Adapter<ViewHolder>(){
//todo likes dislikes count, like then fill color of like button same for dislike
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtPackage: TextView = view.findViewById<TextView>(R.id.txtPackage)
        val overallRating: RatingBar = view.findViewById<RatingBar>(R.id.overallRating)
        val accommodationRating: AppCompatRatingBar = view.findViewById<AppCompatRatingBar>(R.id.accommodationRating)
        val travelRating: AppCompatRatingBar = view.findViewById<AppCompatRatingBar>(R.id.travelRating)
        val foodRating: AppCompatRatingBar = view.findViewById<AppCompatRatingBar>(R.id.foodRating)
        val tvReview: TextView = view.findViewById<TextView>(R.id.tvReview)
        val txtDate: TextView = view.findViewById<TextView>(R.id.txtDate)
        val txtAuthor: TextView = view.findViewById<TextView>(R.id.txtAuthor)
    val txtComment: TextView = view.findViewById<TextView>(R.id.txtComment)
    val llEdit: LinearLayout = view.findViewById<LinearLayout>(R.id.llEdit)
    val llDelete: LinearLayout = view.findViewById<LinearLayout>(R.id.llDelete)
    val llAction: LinearLayout = view.findViewById<LinearLayout>(R.id.llAction)
    val llReply: LinearLayout = view.findViewById<LinearLayout>(R.id.llReply)
    val llComment: LinearLayout = view.findViewById<LinearLayout>(R.id.llComment)
    val llDetail: LinearLayout = view.findViewById<LinearLayout>(R.id.llDetail)

//        init{
//            llDetail.setOnClickListener(this)
//        }
//
//        override fun onClick(view: View?) {
//            val position: Int = adapterPosition
////            if(position!=RecyclerView.NO_POSITION){ //is position still valid
//            listener.onItemClick(position)
//
////            }
//        }
    }

//    interface OnItemClickListener{
//        fun onItemClick(position: Int)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtPackage.text = reviewDataset[position].packagesName
        holder.overallRating.rating = reviewDataset[position].overallRating
        holder.accommodationRating.rating = reviewDataset[position].accommodationRating
        holder.travelRating.rating = reviewDataset[position].transportRating
        holder.foodRating.rating = reviewDataset[position].foodRating
        holder.tvReview.text = reviewDataset[position].overall
        holder.txtDate.text = reviewDataset[position].postedDate
        holder.txtAuthor.text = reviewDataset[position].author
        //if the user who is the author then decide visibility
        holder.txtAuthor.isVisible = isShowAll
        holder.llEdit.isVisible = !isShowAll
        holder.llDelete.isVisible = !isShowAll
        holder.llAction.isVisible = isShowAll
        if(reviewDataset[position].reply=="")
            holder.llComment.isVisible=false
        else{
            holder.llComment.isVisible=false
            holder.txtComment.text = reviewDataset[position].reply
        }
        holder.llDetail.setOnClickListener {
            val intent = Intent(holder.llDetail.context, ReviewActivity::class.java)
            intent.putExtra("review", reviewDataset[position])//pass review details to the next activity
            holder.llDetail.context.startActivity(intent)
        }
        holder.llReply.setOnClickListener {
            val intent = Intent(holder.llDetail.context, ReplyActivity::class.java)
            intent.putExtra("reviewId", reviewDataset[position].reviewId)//pass review details to the next activity
            holder.llDetail.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int =reviewDataset.size


}