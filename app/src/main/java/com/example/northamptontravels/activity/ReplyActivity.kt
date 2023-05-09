package com.example.northamptontravels.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.northamptontravels.R
import com.example.northamptontravels.utils.Constant
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject

//todo send notification once post reply
class ReplyActivity : AppCompatActivity() {


    val TheChannelID = "ChannelID"
    val TheChannelName = "ChannelName"
    val TheNotificationID = 0

    var replyReview = "${Constant.REVIEW_URL}${Constant.REPLY_REVIEW}"
    var reviewId: Int?=0

    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var navView: NavigationView? = null
    var btnConfirm: AppCompatButton? = null
    var btnBack: AppCompatButton? = null
    var etReply: EditText?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

         reviewId = intent.getIntExtra("reviewId", 0) // get reviewId from adapter item

        etReply = findViewById(R.id.etReply)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnBack = findViewById(R.id.btnBack)

        //set drawer navigation
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle!!)
        toggle!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView?.setNavigationItemSelectedListener {
            setMenu(it.itemId)
            true
        }

        btnConfirm!!.setOnClickListener{
            if (etReply?.text.isNullOrEmpty()){
                Toast.makeText(this, resources.getString(R.string.commentPlease), Toast.LENGTH_SHORT).show()}
            else
            setReply(etReply?.text.toString())
        }
        btnBack!!.setOnClickListener{
            onBackPressed()
        }

    }

    private fun setReply(reply: String) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method. POST,
            replyReview,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(this, obj.get("message").toString(), Toast.LENGTH_LONG).show()
                    sendNotification()
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
                params.put("reviewId", reviewId.toString())
                params.put("reply",reply)
                return params
            }
        }

        queue.add(stringRequest)
    }

    private fun sendNotification() {
        createTheNotificationChannel()
        val builder = NotificationCompat.Builder(this, TheChannelID)
            .setContentTitle("Sample Title")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("This is the message body")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notifManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
//    notifManager.notify(TheNotificationID, builder)

        nextView()
    }

    //direct to home activity
    private fun nextView() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun createTheNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = TheChannelName
            val descriptionText = "moving On"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(TheChannelID, name, importance).apply {
                description = descriptionText
            }

            //register channel with system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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