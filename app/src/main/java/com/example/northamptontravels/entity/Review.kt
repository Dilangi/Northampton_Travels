package com.example.northamptontravels.entity

import android.os.Parcel
import android.os.Parcelable


class Review: Parcelable {

    var reviewId: Int =0
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
    var likedSet: String=""
    var dislikedSet: String=""

    constructor(parcel: Parcel) : this(
        parcel.readInt()?:  0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat() ?: 0f,
        parcel.readString() ?: "",
        parcel.readFloat() ?: 0f,
        parcel.readString() ?: "",
        parcel.readFloat() ?: 0f,
        parcel.readString() ?: "",
        parcel.readFloat() ?: 0f,
        parcel.readString() ?: "",
        parcel.readInt() ?:0,
        parcel.readInt() ?:0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    constructor(
        reviewId: Int,
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
        dislike: Int,
        likedSet: String,
        dislikedSet: String

    ) {
        this.reviewId =reviewId
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
        this.likedSet=likedSet
        this.dislikedSet=dislikedSet
    }

    constructor()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(reviewId)
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
        parcel.writeString(likedSet)
        parcel.writeString(dislikedSet)
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