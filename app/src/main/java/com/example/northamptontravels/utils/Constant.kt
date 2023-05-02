package com.example.northamptontravels.utils

class Constant {
    val url = "http://10.136.197.119/friendly/rank-data-information.php?op=1"; //for university WAN
//    val BASE_URL =
//    val LOGIN_PATH =

    companion object {
        val BASE_URL: String = "http://192.168.0.102/northampton_travels/user.php?op=1"
        val REVIEW_URL: String = "http://192.168.0.102/northampton_travels/review.php?op=1"
        val SIGNUP_PATH: String = "&addUser=1"
        val LOGIN_PATH: String="&getCredentials=1"
        val UPDATE_USER_PATH: String = "&updateUser=1"
        val INSERT_REVIEW: String = "&addReview=1"
    }
}