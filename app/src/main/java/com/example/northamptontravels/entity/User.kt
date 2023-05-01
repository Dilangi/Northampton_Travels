package com.example.northamptontravels.entity

class User(
    firstName: String,
    lastName: String,
    email: String,
    username: String
) {
    constructor(
        firstName: String, lastName: String, email: String, username: String,
        password: String
    ) : this(firstName, lastName, email, username) {
        this.password = password
    }

    constructor(
        firstName: String, lastName: String, email: String, username: String,
        userId: Int
    ) : this(firstName, lastName, email, username) {
        this.userId = userId
    }

    var firstName: String = firstName

    var lastName: String = lastName

    var email: String = email

    var username: String = username

     var userId: Int = 0

     var password: String = ""
}