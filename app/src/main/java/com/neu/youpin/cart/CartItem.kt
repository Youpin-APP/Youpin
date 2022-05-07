package com.neu.youpin.cart


data class CartItem(val name: String, val pic: String, val price: Float, var count: Int,
                    var type: String, var selected : Int, val gid : Int, val caid : Int) {}