package com.example.northamptontravels.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.northamptontravels.R
import com.example.northamptontravels.fragment.FirstFragment
import com.example.northamptontravels.fragment.FourthFragment
import com.example.northamptontravels.fragment.SecondFragment
import com.example.northamptontravels.fragment.ThirdFragment
import com.google.android.material.navigation.NavigationView

class AddReviewActivity : AppCompatActivity() {
    var  btnNext: AppCompatButton? = null
    var  btnBack: AppCompatButton? = null
    var toggle: ActionBarDrawerToggle?=null
    var drawerLayout: DrawerLayout?=null
    var navView: NavigationView?=null
    var ivAll: ImageView?=null
    var ivFood: ImageView?=null
    var ivAccommodation: ImageView?=null
    var ivTransport: ImageView?=null

    val fragment1 = FirstFragment()
    val fragment2 = SecondFragment()
    val fragment3 = ThirdFragment()
    val fragment4 = FourthFragment()
    var fragment: Fragment? = fragment1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        ivAll = findViewById(R.id.ivAll)
        ivTransport = findViewById(R.id.ivTransport)
        ivFood = findViewById(R.id.ivFood)
        ivAccommodation = findViewById(R.id.ivAccommodation)
    btnNext = findViewById(R.id.btnNext)
    btnBack = findViewById(R.id.btnBack)
    drawerLayout=findViewById(R.id.drawerLayout)
    navView = findViewById(R.id.navView)

    toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
    drawerLayout?.addDrawerListener(toggle!!)
    toggle!!.syncState()

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    navView?.setNavigationItemSelectedListener {
        setMenu(it.itemId)
        true
    }

    setFragment()
    btnNext!!.setOnClickListener{
        getFragment()
    }
    btnBack!!.setOnClickListener{
        supportFragmentManager.beginTransaction().apply {
            backPressed()
        }
    }


//        spPackage = findViewById(R.id.sp_package)
//        spPackage?.onItemSelectedListener =object :AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                if(position==0)
//                    Toast.makeText(this@AddReviewActivity, "Please select the package", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this@AddReviewActivity, "Selected ${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {
//            }
//
//        }

    }

    //set previous Fragment and change selected imageview on button click
    private fun backPressed() {
//        supportFragmentManager.beginTransaction().apply {
//            if (fragment == fragment4) {
//                fragment = fragment3
//            }else if(fragment == fragment3) {
//                fragment = fragment2
//            }else if(fragment == fragment2) {
//                fragment = fragment1
//            }
//            replace(R.id.flReview, fragment!!)
//            addToBackStack(null)
//            commit()
//        }


        if (fragment == fragment4) {
            fragment = fragment3
            ivAccommodation!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivTransport!!.setBackgroundResource(R.drawable.grey_btn)
        }else if(fragment == fragment3) {
            fragment = fragment2
            ivFood!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivAccommodation!!.setBackgroundResource(R.drawable.grey_btn)
        }else if(fragment == fragment2) {
            fragment = fragment1
            ivAll!!.setBackgroundResource(R.drawable.bg_btn_navigate)
            ivFood!!.setBackgroundResource(R.drawable.grey_btn)
        }
        onBackPressed()
    }

    //set next Fragment and change selected imageview on button click
    private fun getFragment(){
        supportFragmentManager.beginTransaction().apply {
            if (fragment == fragment1) {
                fragment = fragment2
                ivFood!!.setBackgroundResource(R.drawable.bg_btn_navigate)
                ivAll!!.setBackgroundResource(R.drawable.grey_btn)
            }else if(fragment == fragment2) {
                fragment = fragment3
                ivAccommodation!!.setBackgroundResource(R.drawable.bg_btn_navigate)
                ivFood!!.setBackgroundResource(R.drawable.grey_btn)
            }else if(fragment == fragment3) {
                fragment = fragment4
                ivTransport!!.setBackgroundResource(R.drawable.bg_btn_navigate)
                ivAccommodation!!.setBackgroundResource(R.drawable.grey_btn)
            }
            replace(R.id.flReview, fragment!!)
            addToBackStack(null)
            commit()
        }
    }

    //set navigation drawer menu
    private fun setMenu(itemId: Int) {
        when(itemId){
            R.id.addReview-> {
                //direct to Addd Review page
                Toast.makeText(this, "Already there", Toast.LENGTH_SHORT).show()
            }
            R.id.myReviews-> {
                //todo set reviews list by reviews of user
//                    setMyReviews()
            }
            R.id.reviews-> {
                //todo set all posted reviews for particular package
//                    setReviews()
            }
            R.id.editProfile-> {
                //direct to Profile Activity
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.logout-> {
                //todo confirmation and logout
//                    showDialog()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle!!.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    // set the first fragment into frame layout
    private fun setFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flReview, fragment1)
            commit()
        }
//        ivAll!!.setBackgroundColor(resources.getColor(R.color.button_theme))
        ivAll!!.setBackgroundResource(R.drawable.bg_btn_navigate)
    }
}