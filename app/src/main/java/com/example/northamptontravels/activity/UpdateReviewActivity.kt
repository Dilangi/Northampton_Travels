package com.example.northamptontravels.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.entity.Review
import com.example.northamptontravels.utils.Constant
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject

class UpdateReviewActivity : AppCompatActivity() {
    var updateReview = "${Constant.REVIEW_URL}${Constant.UPDATE_REVIEW}"

    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null
    var btnBack: AppCompatButton? = null
    var btnUpdate: AppCompatButton? = null

    var spPackage: Spinner? = null
    var etReview: EditText? = null
    var etFood: EditText? = null
    var etTransport: EditText? = null
    var etAccommodation: EditText? = null
    var ratingBar: AppCompatRatingBar? = null
    var ratingFood: AppCompatRatingBar? = null
    var ratingTravel: AppCompatRatingBar? = null
    var ratingAccommodation: AppCompatRatingBar? = null
    var tvMonth: TextView? = null
    var dpVisited: ImageView? = null

    var visitedDate: String? = ""
    var packagesName: String? = null
    lateinit var spAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_review)

        etReview = findViewById(R.id.etReview)
        etFood = findViewById(R.id.etFood)
        etTransport = findViewById(R.id.etTransport)
        etAccommodation = findViewById(R.id.etAccommodation)
        ratingBar = findViewById(R.id.ratingBar)
        ratingFood = findViewById(R.id.ratingFood)
        ratingTravel = findViewById(R.id.ratingTravel)
        ratingAccommodation = findViewById(R.id.ratingAccommodation)
        tvMonth = findViewById(R.id.tvMonth)
        dpVisited = findViewById(R.id.dpVisited)
        spPackage = findViewById(R.id.spPackage)
        btnBack = findViewById(R.id.btnBack)
        btnUpdate = findViewById(R.id.btnUpdate)

        //set drawer navigation
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle!!)
        toggle!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView?.setNavigationItemSelectedListener {
            setMenu(it.itemId)
            true
        }

        val packageList = resources.getStringArray(R.array.tourPackage)
        spAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, packageList)
        spPackage?.adapter = spAdapter

        var review = intent.getParcelableExtra<Review>("review")
        if (review != null)
            setReview(review!!)
        else
            Toast.makeText(this, resources.getString(R.string.close), Toast.LENGTH_SHORT).show()


        //listener for the spinner
        spPackage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                review!!.packagesName = adapterView!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                packagesName = packageList[0]
            }
        }

        //listener for rating bar
        ratingBar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            review!!.overallRating = ratingBar.rating
        }
        ratingFood?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            review!!.foodRating = ratingFood!!.rating
        }
        ratingAccommodation?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            review!!.accommodationRating = ratingAccommodation!!.rating
        }
        ratingTravel?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            review!!.transportRating = ratingTravel!!.rating
        }

//        dpVisited.setOnDateChangedListener()
        dpVisited?.setOnClickListener()
        {
            getVisitedDate(review!!)
        }

        //set action listener for tap on button
        btnBack!!.setOnClickListener {
            onBackPressed()
        }
        btnUpdate!!.setOnClickListener {
            if (packagesName == packageList[0])
                Toast.makeText(
                    this,
                    resources.getString(R.string.selectPackage),
                    Toast.LENGTH_SHORT
                ).show()
            else if (review!!.overallRating == 0f || review!!.foodRating == 0f || review!!.transportRating == 0f || review!!.accommodationRating == 0f)
                Toast.makeText(this, resources.getString(R.string.addRating), Toast.LENGTH_SHORT)
                    .show()
            else {
                if (!etReview?.text.isNullOrEmpty())
                    review!!.overall = etReview!!.text.toString()
                if (!etAccommodation?.text.isNullOrEmpty())
                    review!!.accommodation = etAccommodation!!.text.toString()
                if (!etFood?.text.isNullOrEmpty())
                    review!!.food = etFood!!.text.toString()
                if (!etTransport?.text.isNullOrEmpty())
                    review!!.transport = etTransport!!.text.toString()
                updateCall(review!!)
            }
        }

    }

    private fun setReview(review: Review) {
        val spinnerPosition: Int = spAdapter.getPosition(review.packagesName)
        spPackage!!.setSelection(spinnerPosition)
        ratingBar!!.rating = review.overallRating
        ratingFood!!.rating = review.foodRating
        ratingTravel!!.rating = review.transportRating
        ratingAccommodation!!.rating = review.accommodationRating
        tvMonth!!.text = review.visitedDate
        etReview!!.setText(review.overall)
        etAccommodation!!.setText(review.accommodation)
        etFood!!.setText(review.food)
        etTransport!!.setText(review.transport)
        Log.d("DILLL", "setReview: " + review.reviewId)
    }

    //pick and set date selected to text view
    private fun getVisitedDate(review: Review) {
        val calender = Calendar.getInstance()
        val visitedYr = calender.get(Calendar.YEAR)
        val visitedMonth = calender.get(Calendar.MONTH)
        val visitedDay = calender.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            visitedDate = "$dayOfMonth/${month + 1}/$year"
            tvMonth?.setText(visitedDate)
            review?.visitedDate = visitedDate as String
        }, visitedYr, visitedMonth, visitedDay)

        datePickerDialog.show()
    }

    private fun updateCall(review: Review) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST,
            updateReview,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(this, obj.get("message").toString(), Toast.LENGTH_LONG).show()
                    nextView()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    if (error != null) {
                        Toast.makeText(
                            applicationContext,
                            error.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("reviewId", review!!.reviewId.toString())
                params.put("packagesName", review!!.packagesName)
                params.put("overallRating", review!!.overallRating.toString())
                params.put("accommodationRating", review!!.accommodationRating.toString())
                params.put("foodRating", review!!.foodRating.toString())
                params.put("transportRating", review!!.transportRating.toString())
                params.put("visitedDate", review!!.visitedDate)
                params.put("overall", review!!.overall)
                params.put("food", review!!.food)
                params.put("accommodation", review!!.accommodation)
                params.put("transport", review!!.transport)
                return params
            }
        }

        queue.add(stringRequest)
    }

    private fun nextView() {
        //direct to Addd Review page
        val intent = Intent(this, MyReviewsActivity::class.java)
        startActivity(intent)
    }


    //set navigation drawermenu
    private fun setMenu(itemId: Int) {
        when (itemId) {
            R.id.home -> {
                //direct to Addd Review page
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }

            R.id.addReview -> {
                //direct to Addd Review page
                val intent = Intent(this, AddReviewActivity::class.java)
                startActivity(intent)
            }
            R.id.myReviews -> {
                //direct to MyReviews page
                val intent = Intent(this, MyReviewsActivity::class.java)
                startActivity(intent)
            }
            R.id.editProfile -> {
                //direct to Profile Activity
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                showDialog(this)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle!!.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun showDialog(context: Context) {
            val builder = AlertDialog.Builder(context)

            // Set the dialog message
            builder.setMessage(context.resources.getString(R.string.logoutConfirmation))
                .setCancelable(false)
                .setPositiveButton(context.resources.getString(R.string.confirm)) { dialog, _ -> // Positive button action
                    val preferences =
                        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
                    preferences.edit().clear().apply()

                    dialog.dismiss()

                    //direct to Login page
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
                .setNegativeButton(context.resources.getString(R.string.close)) { dialog, _ -> // Negative button action
                    dialog.dismiss()
                }

            // Create and show the dialog box
            val dialog = builder.create()
            dialog.show()
        }
    }
}