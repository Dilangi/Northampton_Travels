package com.example.northamptontravels.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.entity.User
import com.example.northamptontravels.utils.Constant
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {
    var updateUser = "${Constant.BASE_URL}${Constant.UPDATE_USER}"

    //initiate variable
    private var username: String? = ""
    private var email: String? = ""
    private var lastName: String? = ""
    private var firstName: String? = ""
    private var userId: Int = 0
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etUsername: EditText? = null

    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null
    private var btnSave: AppCompatButton? = null
    private var btnBack: AppCompatButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //assign UI components to variables
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

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

        setUserDetails()

        //set action listener for tap on button
        btnSave!!.setOnClickListener {
            updateDetails()
        }

        btnBack!!.setOnClickListener {
            nextView()
        }
    }

    private fun setUserDetails() {
        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        firstName = preferences.getString("firstName", "")
        lastName = preferences.getString("lastName", "")
        username = preferences.getString("username", "")
        email = preferences.getString("email", "")
        userId = preferences.getInt("userId", 0)
        etFirstName!!.setText(firstName)
        etLastName!!.setText(lastName)
        etUsername!!.setText(username)
        etEmail!!.setText(email)
    }

    private fun updateDetails() {
        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        if (firstName != etFirstName?.text.toString()) {
            firstName = etFirstName?.text.toString()
            editor.putString("firstName", firstName)
        }

        if (lastName != etLastName?.text.toString()) {
            lastName = etLastName?.text.toString()
            editor.putString("lastName", lastName)
        }

        if (email != etEmail?.text.toString()) {
            email = etEmail?.text.toString()
            editor.putString("email", email)
        }

        if (username != etUsername?.text.toString()) {
            username = etUsername?.text.toString()
            editor.putString("username", username)
        }
        editor.apply()
        editor.commit()

        apiCall()
    }

    //call HTTP request for update user details
    private fun apiCall() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST,
            updateUser,
            Response.Listener<String> { response ->
                try {
                    Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show()
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
                params.put("firstName", firstName!!)
                params.put("lastName", lastName!!)
                params.put("email", email!!)
                params.put("username", username!!)
                params.put("userId", userId.toString()!!)
                return params
            }
        }

        queue.add(stringRequest)
    }

    //direct to Home page
    private fun nextView() {
        val intent = Intent(this, HomeActivity::class.java)
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
                UpdateReviewActivity.showDialog(this)
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