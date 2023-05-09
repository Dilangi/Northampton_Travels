package com.example.northamptontravels.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.no.UpdateReviewActivity
import com.example.northamptontravels.R
import com.example.northamptontravels.activity.ReplyActivity
import com.example.northamptontravels.activity.ReviewActivity
import com.example.northamptontravels.adapter.ReviewAdapter.ViewHolder
import com.example.northamptontravels.entity.Review
import com.example.northamptontravels.utils.Constant
import org.json.JSONException
import org.json.JSONObject

class ReviewAdapter (private val reviewDataset:ArrayList<Review>,
                     private val isShowAll: Boolean) : RecyclerView.Adapter<ViewHolder>(){
//todo likes dislikes count, like then fill color of like button same for dislike


    var updateLikes = "${Constant.REVIEW_URL}${Constant.LIKES_REVIEW}"
    var updateDislike = "${Constant.REVIEW_URL}${Constant.DISLIKE_REVIEW}"
    var deleteReview = "${Constant.REVIEW_URL}${Constant.DELETE_REVIEW}"

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
    val ivLikes: ImageView = view.findViewById<ImageView>(R.id.ivLikes)
    val ivDislikes: ImageView = view.findViewById<ImageView>(R.id.ivDislikes)
    val txtLikesCount: TextView = view.findViewById<TextView>(R.id.txtLikesCount)
    val txtDislikeCount: TextView = view.findViewById<TextView>(R.id.txtDislikeCount)

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
            holder.llComment.isVisible=true
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
        holder.llEdit.setOnClickListener{
            val intent = Intent(holder.llEdit.context, UpdateReviewActivity::class.java)
            intent.putExtra("review", reviewDataset[position])//pass review details to the next activity
            holder.llEdit.context.startActivity(intent)
        }
        holder.llDelete.setOnClickListener{
            val intent = Intent(holder.llDelete.context, UpdateReviewActivity::class.java)
            intent.putExtra("reviewId", reviewDataset[position].reviewId)//pass review details to the next activity
            holder.llDelete.context.startActivity(intent)
        }
        holder.ivLikes.setOnClickListener{
            //todo if already liked a post then
            holder.ivLikes.setImageResource(R.drawable.ic_like_filled)
            holder.txtLikesCount.text=(reviewDataset[position].likes +1).toString()
            addLike(reviewDataset[position].likes +1,holder.txtLikesCount.context,reviewDataset[position]) // increase like count by one
        }
        holder.ivDislikes.setOnClickListener{
            holder.ivDislikes.setImageResource(R.drawable.ic_dislike_filled)
            holder.txtDislikeCount.text=(reviewDataset[position].dislike +1).toString()
            addDislike(reviewDataset[position].dislike +1,holder.txtDislikeCount.context,reviewDataset[position]) // increase dislike count by one
        }

    }

    private fun addDislike(i: Int, context: Context, review: Review) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method. POST,
            updateDislike,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Log.d("Test", "addDislike: "+obj.get("message").toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Log.d("Test", "addDislike: "+error.message.toString())
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("reviewId", review!!. reviewId.toString())
                params.put("dislike", review!!.dislike.toString())
                return params
            }
        }

        queue.add(stringRequest)
    }

    private fun addLike(i: Int, context: Context, review: Review) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method. POST,
            updateLikes,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Log.d("Test", "addDislike: "+obj.get("message").toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Log.d("Test", "addDislike: "+error.message.toString())
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("reviewId", review!!. reviewId.toString())
                params.put("likes", review!!.likes.toString())
                return params
            }
        }

        queue.add(stringRequest)
    }

    override fun getItemCount(): Int =reviewDataset.size


}