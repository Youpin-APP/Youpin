package com.neu.youpin.entity

import android.app.Application

public class UserApplication: Application(){
    private var id = 0
    private var name: String? = null
    private var pass: String? = null

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getPass(): String? {
        return pass
    }

    fun setPass(pass: String?) {
        this.pass = pass
    }

}