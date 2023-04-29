package com.example.northamptontravels.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.northamptontravels.R

class ReviewAdapter (private val reviewDataset:ArrayList<String>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
//        val txtName = view.findViewById<TextView>(R.id.txtName)
//        val btnButton = view.findViewById<Button>(R.id.btnButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review, parent, false)
        return ReviewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
//        holder.txtName.text = reviewDataset[position]
    }

    override fun getItemCount(): Int =reviewDataset.size


}