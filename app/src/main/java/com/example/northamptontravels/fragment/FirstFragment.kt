package com.example.northamptontravels.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import com.example.northamptontravels.R
import com.example.northamptontravels.entity.Review

class FirstFragment : Fragment() {
    //initiate variable
    var spPackage: Spinner? = null
    var etReview: EditText? = null
    var ratingBar: AppCompatRatingBar? = null
    var tvMonth: TextView? = null
    var dpVisited: ImageView? = null

    var visitedDate: String? = ""
    var packagePosition: Int? = 0
    var ratingAll: Float? = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //assign UI components to variables
        etReview = getView()?.findViewById(R.id.etReview)
        ratingBar = getView()?.findViewById(R.id.ratingBar)
        tvMonth = getView()?.findViewById(R.id.tvMonth)
        spPackage = getView()?.findViewById(R.id.spPackage)
        dpVisited = getView()?.findViewById(R.id.dpVisited)

        //set spinner values
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tourPackage,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPackage?.adapter = adapter
        }

        //listener for the spinner
        spPackage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                packagePosition = position
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

        }

        //listener for rating bar
        ratingBar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingAll = ratingBar.rating
        }

        dpVisited?.setOnClickListener()
        {
            getVisitedDate()
        }
    }

    //pick and set date selected to text view
    private fun getVisitedDate() {
        val calender = Calendar.getInstance()
        val visitedYr = calender.get(Calendar.YEAR)
        val visitedMonth = calender.get(Calendar.MONTH)
        val visitedDay = calender.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            visitedDate = "$dayOfMonth/${month + 1}/$year"
            tvMonth?.setText(visitedDate)
        }, visitedYr, visitedMonth, visitedDay)

        datePickerDialog.show()
    }

    //set values to Review object
    fun getFirstReviewData(): Review {
        val reviewData = Review()
        if (packagePosition == 0) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.selectPackage),
                Toast.LENGTH_SHORT
            ).show()
        } else if (ratingAll == 0f) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.addRating),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val packages = resources.getStringArray(R.array.tourPackage)
            reviewData.packagesName = packages[packagePosition!!]
            reviewData.overall = etReview?.text.toString()
            reviewData.overallRating = ratingAll!!
            reviewData.visitedDate = visitedDate!!
        }
        return reviewData

}
}
