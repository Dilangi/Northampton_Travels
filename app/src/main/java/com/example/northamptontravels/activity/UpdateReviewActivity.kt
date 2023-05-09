package com.example.no

import com.example.northamptontravels.activity.AddReviewActivity
import com.example.northamptontravels.activity.MyReviewsActivity
import com.example.northamptontravels.activity.ProfileActivity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
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
import com.example.northamptontravels.entity.User
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
    var overallStars: Float? = 0f
    var travelStars: Float? = 0f
    var accommodationStars: Float? = 0f
    var foodStars: Float? = 0f
    var review: Review?= Review()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_review)

        review= intent.getParcelableExtra<Review>("review")
//        if(reviewObj!=null)
//            setReview(reviewObj)
//        else
//            Toast.makeText(this,resources.getString(R.string.close),Toast.LENGTH_SHORT).show()

        etReview = findViewById(R.id.etReview)
        etFood = findViewById(R.id.etFood)
        etTransport= findViewById(R.id.etTransport)
        etAccommodation = findViewById(R.id.etAccommodation)
        ratingBar= findViewById(R.id.ratingBar)
        ratingFood= findViewById(R.id.ratingFood)
        ratingTravel= findViewById(R.id.ratingTravel)
        ratingAccommodation= findViewById(R.id.ratingAccommodation)
        tvMonth= findViewById(R.id.tvMonth)
        dpVisited= findViewById(R.id.dpVisited)
        spPackage= findViewById(R.id.spPackage)
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

        val packageList =resources.getStringArray(R.array.tourPackage)
        val spAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,packageList)
        spPackage?.adapter=spAdapter

        //listener for the spinner
        spPackage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                packagesName= adapterView!!.getItemAtPosition(position).toString()
                review!!.packagesName= packagesName as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                packagesName=packageList[0]
            }}

        //listener for rating bar
        ratingBar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            overallStars = ratingBar.rating
            review!!.overallRating= overallStars as Float
        }
        ratingFood?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            foodStars = ratingBar.rating
            review!!.foodRating= foodStars as Float
        }
        ratingAccommodation?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            accommodationStars = ratingBar.rating
            review!!.accommodationRating= accommodationStars as Float
        }
        ratingTravel?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            travelStars = ratingBar.rating
            review!!.transportRating= travelStars as Float
        }

//        dpVisited.setOnDateChangedListener()
        dpVisited?.setOnClickListener()
        {
            getVisitedDate()
        }

        //set action listener for tap on button
        btnBack!!.setOnClickListener {
            onBackPressed()
        }
        btnUpdate!!.setOnClickListener {
            if(packagesName==packageList[0])
                Toast.makeText(this,resources.getString(R.string.selectPackage),Toast.LENGTH_SHORT).show()
            else if(overallStars==0f || foodStars==0f || travelStars==0f || accommodationStars==0f)
                Toast.makeText(this,resources.getString(R.string.addRating),Toast.LENGTH_SHORT).show()
            else{
                if(!etReview?.text.isNullOrEmpty())
                    review!!.overall= etReview!!.text.toString()
                if(!etAccommodation?.text.isNullOrEmpty())
                    review!!.accommodation= etAccommodation!!.text.toString()
                if(!etFood?.text.isNullOrEmpty())
                    review!!.food= etFood!!.text.toString()
                if(!etTransport?.text.isNullOrEmpty())
                    review!!.transport= etTransport!!.text.toString()
                updateCall()
            }}

    }

    //pick and set date selected to text view
    private fun getVisitedDate() {
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

    private fun updateCall() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method. POST,
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
                params.put("reviewId", review!!. reviewId.toString())
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
            R.id.reviews -> {
                //todo set all posted reviews for particular package, make this home
//                    setReviews()
            }
            R.id.editProfile -> {
                //direct to Profile Activity
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                //todo confirmation and logout
//                    showDialog()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle!!.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}