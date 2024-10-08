package com.example.northamptontravels.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRatingBar
import com.example.northamptontravels.R
import com.example.northamptontravels.entity.Review

class ThirdFragment : Fragment() {
    //initiate variable
    var etReview: EditText? = null
    var ratingBar: AppCompatRatingBar? = null
    var ratingTransport: Float? = 0f

    var reviewData: Review? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //assign UI components to variables
        etReview = getView()?.findViewById(R.id.etReview)
        ratingBar = getView()?.findViewById(R.id.ratingBar)
        reviewData = arguments?.getParcelable<Review>("data")

        //listener for rating bar
        ratingBar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingTransport = ratingBar.rating
        }
    }

    //set values to Review object
    fun getThirdReviewData(): Review {
        if (reviewData  == null) {
            reviewData = Review()
        }

        if (ratingTransport == 0f) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.addRating),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            reviewData!!.transport = etReview?.text.toString()
            reviewData!!.transportRating = ratingTransport!!
        }
        return reviewData as Review

    }

}