package com.example.northamptontravels.entity

import android.os.Parcel
import android.os.Parcelable


class Review: Parcelable {

     var packagesName: String = ""
    var overall: String=""
    var author: String=""
    var visitedDate: String=""
    var postedDate: String=""
    var overallRating: Float = 0.0f
    var food: String=""
    var foodRating: Float = 0.0f
    var transport: String=""
    var transportRating: Float = 0.0f
    var accommodation: String=""
    var accommodationRating: Float = 0.0f
    var reply: String=""
     var likes: Int = 0
    var dislike: Int= 0

    constructor(parcel: Parcel) : this(
        TODO("packageName"),
        TODO("overall"),
        TODO("author"),
        TODO("visitedDate"),
        TODO("postedDate"),
        TODO("overallRating"),
        TODO("food"),
        TODO("foodRating"),
        TODO("transport"),
        TODO("transportRating"),
        TODO("accommodation"),
        TODO("accommodationRating"),
        TODO("reply"),
        TODO("likes"),
        TODO("dislike")
    ) {
        packagesName = parcel.readString()!!
        overall = parcel.readString()!!
        author = parcel.readString()!!
        visitedDate = parcel.readString()!!
        postedDate = parcel.readString()!!
        overallRating = parcel.readFloat()
        food = parcel.readString()!!
        foodRating = parcel.readFloat()
        transport = parcel.readString()!!
        transportRating = parcel.readFloat()
        accommodation = parcel.readString()!!
        accommodationRating = parcel.readFloat()
        reply = parcel.readString()!!
        likes = parcel.readInt()
        dislike = parcel.readInt()
    }

    constructor(
        packagesName: String,
        overall: String,
        author: String,
        visitedDate: String,
        postedDate: String,
        overallRating: Float,
        food: String,
        foodRating: Float,
        transport: String,
        transportRating: Float,
        accommodation: String,
        accommodationRating: Float,
        reply: String,
        likes: Int,
        dislike: Int

    ) {
        this.packagesName =packagesName
        this.overall=overall
        this.author=author
        this.visitedDate=visitedDate
        this.postedDate=postedDate
        this.overallRating=overallRating
        this.food=food
        this.foodRating=foodRating
        this.transport=transport
        this.transportRating=transportRating
        this.accommodation=accommodation
        this.accommodationRating=accommodationRating
        this.reply=reply
        this.likes=likes
        this.dislike=dislike
    }

    constructor()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(packagesName)
        parcel.writeString(overall)
        parcel.writeString(author)
        parcel.writeString(visitedDate)
        parcel.writeString(postedDate)
        parcel.writeFloat(overallRating)
        parcel.writeString(food)
        parcel.writeFloat(foodRating)
        parcel.writeString(transport)
        parcel.writeFloat(transportRating)
        parcel.writeString(accommodation)
        parcel.writeFloat(accommodationRating)
        parcel.writeString(reply)
        parcel.writeInt(likes)
        parcel.writeInt(dislike)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Review> {
        override fun createFromParcel(parcel: Parcel): Review {
            return Review(parcel)
        }

        override fun newArray(size: Int): Array<Review?> {
            return arrayOfNulls(size)
        }
    }
}