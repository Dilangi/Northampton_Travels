package com.example.northamptontravels.utils

class Constant {
    val url = "http://10.136.197.119/friendly/rank-data-information.php?op=1"; //for university WAN

    companion object {
        val BASE_URL: String = "http://192.168.0.102/northampton_travels/user.php?op=1"
        val REVIEW_URL: String = "http://192.168.0.102/northampton_travels/review.php?op=1"
        val SIGNUP_PATH: String = "&addUser=1"
        val LOGIN_PATH: String="&getCredentials=1"
        val UPDATE_USER: String = "&updateUser=1"
        val INSERT_REVIEW: String = "&addReview=1"
        val DELETE_REVIEW: String = "&deleteReview=1"
        val PACKAGE_REVIEWS: String = "&packageReviews=1"
        val MY_REVIEWS: String = "&authorReviews=1"
        val REPLY_REVIEW: String = "&reviewReply=1"
        val UPDATE_REVIEW: String = "&updateReview=1"
        val LIKES_REVIEW: String = "&addLikes=1"
        val DISLIKE_REVIEW: String = "&addDislike=1"
        val ALL_REVIEWS: String = "&allReviews=1"
    }
}