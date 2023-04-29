package com.example.northamptontravels.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    //    var url = "http://10.136.197.119/friendly/rank-data-information.php?op=1"; //for university WAN
    var url = "http://192.168.0.102/northampton_travels/user.php?op=1"
    var getCredentials = url + "&getCredentials=1"

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

            if (isValidUser(username, password)) {
                //direct to Home page
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else
                Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidUser(username: String, password: String): Boolean {
        var getPassword =""

        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.GET,
            getCredentials,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    getPassword = obj.getString("password")
                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
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

        return (password==getPassword)
    }
}