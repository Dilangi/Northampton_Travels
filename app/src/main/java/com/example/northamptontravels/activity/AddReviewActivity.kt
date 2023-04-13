package com.example.northamptontravels.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.example.northamptontravels.R

class AddReviewActivity : AppCompatActivity() {

//    var spPackage: Spinner?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

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
}