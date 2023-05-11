package com.example.northamptontravels.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.entity.User
import com.example.northamptontravels.utils.Constant
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    var getCredentials = "${Constant.BASE_URL}${Constant.LOGIN_PATH}"

    //initiate variable
    private var etUsername: EditText? = null
    private var etPassword: EditText? = null
    private var tvSignup: TextView? = null
    private var btnLogin: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //assign UI components to variables
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvSignup = findViewById(R.id.tvSignup)
        btnLogin = findViewById(R.id.btnLogin)

        //set action listener for tap on button
        btnLogin!!.setOnClickListener {
            validate()
        }

        tvSignup!!.setOnClickListener {
            ////direct to signup page
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    //text field validation.
    private fun validate() {
        if (etUsername?.text.isNullOrEmpty())
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show()
        else if (etPassword?.text.isNullOrEmpty())
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
        else {//required fields are filled
            var username = etUsername?.text.toString()
            var password = etPassword?.text.toString()

            isValidUser(username, password) }
    }

    private fun isValidUser(username: String, password: String) {
//HTTP request to get user details where username = username entered
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST,
            getCredentials,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)

                    val gson = Gson()
                    val user: User = gson.fromJson(obj.get("user").toString(), User::class.java)
                    if(password==user.password){
                        Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show()
                        saveUser(user)
                        //direct to Home page
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        else
                        Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()

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
                params.put("username", username)
                return params
            }
        }

        queue.add(stringRequest)


    }

    //put user details in shared preferences
    private fun saveUser(user: User) {
        var isAdmin: Boolean? = false
        if(user.email.contains("northamptontravels"))
            isAdmin=true

        val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("userId", user.userId)
        editor.putString("username", user.username)
        editor.putString("email", user.email)
        editor.putString("firstName", user.firstName)
        editor.putString("lastName", user.lastName)
        editor.putBoolean("isAdmin", isAdmin!!)
        editor.apply()
        editor.commit()
    }
}