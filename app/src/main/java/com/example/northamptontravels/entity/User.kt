package com.example.northamptontravels.entity

class User(
    firstName: String?,
    lastName: String,
    email: String,
    username: String,
    password: String
) {

    var firstName: String? = firstName
//        set(firstName) {
//            this.firstName = firstName
//        }

    var lastName: String = lastName

    var email: String = email

    var username: String = username

    var password: String = password
}