package com.neu.youpin.entity

import android.app.Application

public class UserApplication: Application(){
    private var _uid : String? = null
    private var _name: String? = null
    private var _token: String? = null
    private var _isLogin: Boolean = false

    companion object{
        private var instance: UserApplication? = null

        fun getInstance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getId(): String? {
        return _uid
    }

    fun setId(uid: String) {
        this._uid = uid
    }

    fun getName(): String? {
        return _name
    }

    fun setName(name: String?) {
        this._name = name
    }

    fun getToken(): String? {
        return _token
    }

    fun setToken(token: String?) {
        this._token = token
        this._isLogin = true
    }

    // 退出登陆时调用
    fun clearToken(){
        this._isLogin = false
        this._token = null
        this._name = null
        this._uid = null
    }

    fun isLogin(): Boolean{
        return this._isLogin
    }


}