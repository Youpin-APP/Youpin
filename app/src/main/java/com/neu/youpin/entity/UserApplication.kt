package com.neu.youpin.entity

import android.app.Application
import android.content.Context
import com.neu.youpin.login.LoginToken

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
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("isRemember", false)
        if (isRemember){
            this._uid = prefs.getString("uid", this._uid)
            this._name = prefs.getString("name", this._name)
            this._token = prefs.getString("token", this._token)
            this._isLogin = true
        }
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
    }

    fun setLoginToken(loginToken: LoginToken){
        this._uid = loginToken.uid
        this._name = loginToken.name
        this._token = loginToken.token
        this._isLogin = true
        val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        editor.putString("uid", this._uid)
        editor.putString("name", this._name)
        editor.putString("token", this._token)
        editor.putBoolean("isRemember", true)
        editor.apply()
    }

    fun clearLoginToken(){
        clearToken()
        val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
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