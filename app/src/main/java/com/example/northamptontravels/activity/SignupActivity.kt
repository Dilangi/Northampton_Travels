package com.example.northamptontravels.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.entity.User
import com.example.northamptontravels.utils.Constant
import org.json.JSONException
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    var addUser = "${Constant.BASE_URL}${Constant.SIGNUP_PATH}"

    //initiate variable
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etUsername: EditText? = null
    private var etPassword: EditText? = null
    private var etConfirmPassword: EditText? = null
    private var btnRegister: AppCompatButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //assign UI components to variables
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        //set action listener for tap on button
        btnRegister!!.setOnClickListener {
            validate()
        }
    }

    //text field validation.
    private fun validate() {
        if (etFirstName?.text.isNullOrEmpty())
            Toast.makeText(this, resources.getString(R.string.firstNamePlease), Toast.LENGTH_SHORT).show()
        else if (etEmail?.text.isNullOrEmpty())
            Toast.makeText(this, resources.getString(R.string.emailPlease), Toast.LENGTH_SHORT).show()
        else if (etUsername?.text.isNullOrEmpty())
            Toast.makeText(this, resources.getString(R.string.usernamePlease), Toast.LENGTH_SHORT).show()
        else if (etPassword?.text.isNullOrEmpty())
            Toast.makeText(this, resources.getString(R.string.passwordPlease), Toast.LENGTH_SHORT).show()
        else if (etConfirmPassword?.text.isNullOrEmpty())
            Toast.makeText(this, resources.getString(R.string.confirmPasswordPlease), Toast.LENGTH_SHORT).show()
        else {//required fields are filled
            var password = etPassword?.text.toString()
            var confirmPassword = etConfirmPassword?.text.toString()
            if (password == confirmPassword) { //password set correctly
                var firstName = etFirstName?.text.toString()
                var lastName = etLastName?.text.toString()
                var email = etEmail?.text.toString()
                var username = etUsername?.text.toString()

                val user =
                    User(firstName, lastName, email, username, password) //create the user object
                registerUser(user)
            } else
                Toast.makeText(this, resources.getString(R.string.passwordMissmatch), Toast.LENGTH_SHORT).show()
        }
    }

    //insert user to the database
    private fun registerUser(user: User) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method. POST,
            addUser,
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
                params.put("firstName", user.firstName!!)
                params.put("lastName", user.lastName!!)
                params.put("email", user.email!!)
                params.put("username", user.username!!)
                params.put("password", user.password!!)
                return params
            }
        }

        queue.add(stringRequest)

    }

    //direct to Login page
    private fun nextView() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}