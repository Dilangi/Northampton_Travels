package com.example.northamptontravels.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.activity.MyReviewsActivity
import com.example.northamptontravels.activity.UpdateReviewActivity
import com.example.northamptontravels.activity.ReplyActivity
import com.example.northamptontravels.activity.ReviewActivity
import com.example.northamptontravels.adapter.ReviewAdapter.ViewHolder
import com.example.northamptontravels.entity.Review
import com.example.northamptontravels.utils.Constant
import org.json.JSONException
import org.json.JSONObject

class ReviewAdapter(
    private val reviewDataset: ArrayList<Review>,
    private val isShowAll: Boolean,
    private val userId: String,
    private val isAdmin: Boolean,
    private val context: Context
) : RecyclerView.Adapter<ViewHolder>() {

    private var disliked: Boolean = false
    private var liked: Boolean = false
    var updateLikes = "${Constant.REVIEW_URL}${Constant.LIKES_REVIEW}"
    var updateDislike = "${Constant.REVIEW_URL}${Constant.DISLIKE_REVIEW}"
    var deleteReview = "${Constant.REVIEW_URL}${Constant.DELETE_REVIEW}"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtPackage: TextView = view.findViewById<TextView>(R.id.txtPackage)
        val overallRating: RatingBar = view.findViewById<RatingBar>(R.id.overallRating)
        val accommodationRating: AppCompatRatingBar =
            view.findViewById<AppCompatRatingBar>(R.id.accommodationRating)
        val travelRating: AppCompatRatingBar =
            view.findViewById<AppCompatRatingBar>(R.id.travelRating)
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
    }

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
        holder.llReply.isVisible = isAdmin

        if (reviewDataset[position].likedSet.contains(userId)) {
            holder.ivLikes.setImageResource(R.drawable.ic_like_filled)
            liked = true
        } else {
            holder.ivLikes.setImageResource(R.drawable.ic_likes_24)
            liked = false
        }

        if (reviewDataset[position].dislikedSet.contains(userId)) {
            disliked = true
            holder.ivDislikes.setImageResource(R.drawable.ic_dislike_filled)
        } else {
            disliked = false
            holder.ivDislikes.setImageResource(R.drawable.ic_dislike_24)
        }

        holder.txtLikesCount.text = reviewDataset[position].likes.toString()
        holder.txtDislikeCount.text = reviewDataset[position].dislike.toString()

        if (reviewDataset[position].reply == "")
            holder.llComment.isVisible = false
        else {
            holder.llComment.isVisible = true
            holder.txtComment.text = reviewDataset[position].reply
        }
        holder.llDetail.setOnClickListener {
            val intent = Intent(holder.llDetail.context, ReviewActivity::class.java)
            intent.putExtra(
                "review",
                reviewDataset[position]
            )//pass review details to the next activity
            holder.llDetail.context.startActivity(intent)
        }
        holder.llReply.setOnClickListener {
            val intent = Intent(holder.llDetail.context, ReplyActivity::class.java)
            intent.putExtra(
                "reviewId",
                reviewDataset[position].reviewId
            )//pass review details to the next activity
            holder.llDetail.context.startActivity(intent)
        }
        holder.llEdit.setOnClickListener {
            val intent = Intent(holder.llEdit.context, UpdateReviewActivity::class.java)
            intent.putExtra(
                "review",
                reviewDataset[position]
            )//pass review details to the next activity
            holder.llEdit.context.startActivity(intent)
        }
        holder.llDelete.setOnClickListener {
            confirmationDialog(reviewDataset[position])
        }
        holder.ivLikes.setOnClickListener {
            if (liked) {
                holder.ivLikes.setImageResource(R.drawable.ic_likes_24)
                liked = false
                if (reviewDataset[position].likes != 0)
                    reviewDataset[position].likes -= 1
                reviewDataset[position].likedSet.replace(userId, "")
            } else {
                if (!reviewDataset[position].likedSet.contains(userId)) { //user cannot give multiple likes
                    holder.ivLikes.setImageResource(R.drawable.ic_like_filled)
                    liked = true
                    reviewDataset[position].likes += 1
                    reviewDataset[position].likedSet = reviewDataset[position].likedSet + userId
                }
            }
            holder.txtLikesCount.text = (reviewDataset[position].likes).toString()
            addLike(
                reviewDataset[position].likes,
                reviewDataset[position].likedSet,
                reviewDataset[position]
            ) // increase like count by one
        }
        holder.ivDislikes.setOnClickListener {
            if (disliked) {
                holder.ivLikes.setImageResource(R.drawable.ic_dislike_24)
                disliked = false
                if (reviewDataset[position].dislike != 0)
                    reviewDataset[position].dislike = reviewDataset[position].dislike - 1
                reviewDataset[position].dislikedSet.replace(userId, "")
            } else {
                if (!reviewDataset[position].dislikedSet.contains(userId)) { //user cannot give multiple dislikes
                    holder.ivDislikes.setImageResource(R.drawable.ic_dislike_filled)
                    disliked = true
                    reviewDataset[position].dislike = reviewDataset[position].dislike + 1
                    reviewDataset[position].dislikedSet =
                        reviewDataset[position].dislikedSet + userId
                }
            }
            holder.txtDislikeCount.text = (reviewDataset[position].dislike).toString()
            addDislike(
                reviewDataset[position].dislike,
                reviewDataset[position].dislikedSet,
                reviewDataset[position]
            ) // increase dislike count by one
        }

    }

    private fun confirmationDialog(review: Review) {
        val builder = AlertDialog.Builder(context)

        // Set the dialog message
        builder.setMessage(context.resources.getString(R.string.deleteConfirmation))
            .setCancelable(false)
            .setPositiveButton(context.resources.getString(R.string.confirm)) { dialog, _ -> // Positive button action
                dialog.dismiss()
                deleteReviewCall(review)
            }
            .setNegativeButton(context.resources.getString(R.string.close)) { dialog, _ -> // Negative button action
                dialog.dismiss()
            }

        // Create and show the dialog box
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteReviewCall(review: Review) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST,
            deleteReview,
            Response.Listener<String> { response ->
                try {
                    val intent = Intent(context, MyReviewsActivity::class.java)
                    context.startActivity(intent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Log.d("Test", "deleteReviewCall: " + error.message.toString())
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("reviewId", review!!.reviewId.toString())
                return params
            }
        }

        queue.add(stringRequest)
    }

    private fun addDislike(dislikes: Int, dislikeSet: String, review: Review) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST,
            updateDislike,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Log.d("Test", "addDislike: " + obj.get("message").toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Log.d("Test", "addDislike: " + error.message.toString())
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("reviewId", review!!.reviewId.toString())
                params.put("dislike", dislikes.toString())
                params.put("dislikedSet", dislikeSet)
                return params
            }
        }

        queue.add(stringRequest)
    }

    private fun addLike(likes: Int, likeSet: String, review: Review) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST,
            updateLikes,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Log.d("Test", "addDislike: " + obj.get("message").toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Log.d("Test", "addDislike: " + error.message.toString())
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("reviewId", review!!.reviewId.toString())
                params.put("likes", likes.toString())
                params.put("likedSet", likeSet)
                return params
            }
        }

        queue.add(stringRequest)
    }

    override fun getItemCount(): Int = reviewDataset.size


}