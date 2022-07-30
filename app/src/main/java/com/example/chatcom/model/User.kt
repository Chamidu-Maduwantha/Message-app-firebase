package com.example.chatcom.model

class User {

    var uid:String?= null
   var name:String?= null
    var gmail:String?= null
    var profileImage:String?= null
    var bio :String? = null

    constructor(){}
    constructor(
        uid:String?,
        name:String?,
        gmail:String?,
        profileImage:String?,
        bio:String?
    ){
        this.uid = uid
       this.name = name
        this.gmail = gmail
        this.profileImage = profileImage
        this.bio = bio

    }
}