package com.example.northamptontravels.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.fido.fido2.api.common.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import kotlin.math.min

class ProfileActivity : AppCompatActivity() {
    var updateUser = "${Constant.BASE_URL}${Constant.UPDATE_USER}"

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    //initiate variable
    private var stringImage: String? = ""
    private var username: String? = ""
    private var email: String? = ""
    private var lastName: String? = ""
    private var firstName: String? = ""
    private var userId: Int = 0
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etUsername: EditText? = null
    private var ivPhotos: ImageView? = null

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
        ivPhotos = findViewById(R.id.ivPhotos)

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

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    onActivityResult(PICK_IMAGE_REQUEST, Activity.RESULT_OK, data)
                }
            }

        ivPhotos!!.setOnClickListener {
            getImage()
        }
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap: Bitmap? = try {
                MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            if (bitmap != null) {
                ivPhotos!!.setImageBitmap(bitmap)

                // resize image quality
                val maxSize = 1024 // Maximum size in pixels for width or height
                val scaleFactor = min(maxSize.toFloat() / bitmap.width, maxSize.toFloat() / bitmap.height)

                val resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width * scaleFactor).toInt(),
                    (bitmap.height * scaleFactor).toInt(),
                    true
                )

                val quality = 30 // Adjust the quality as needed (0-100)
                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

                val byteArray = outputStream.toByteArray()
                stringImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

            }
        }
    }


    private fun setUserDetails() {
        val preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        firstName = preferences.getString("firstName", "")
        lastName = preferences.getString("lastName", "")
        username = preferences.getString("username", "")
        email = preferences.getString("email", "")
        userId = preferences.getInt("userId", 0)
        stringImage = preferences.getString("picture", "")
        etFirstName!!.setText(firstName)
        etLastName!!.setText(lastName)
        etUsername!!.setText(username)
        etEmail!!.setText(email)
        setImage()
    }

    private fun setImage() {
        if (stringImage != "") {
            val imageBytes = Base64.decode(stringImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivPhotos!!.setImageBitmap(bitmap)
        }
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

        if (stringImage != "") {
            editor.putString("picture", stringImage)
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
                    Toast.makeText(this, "Update Success!", Toast.LENGTH_LONG).show()
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
                params.put("picture", stringImage!!)
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