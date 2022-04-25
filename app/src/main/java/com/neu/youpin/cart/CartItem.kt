package com.neu.youpin.cart


data class CartItem(val name: String, val pic: Int, val price: Int, var count: Int,
                    var type: String, var selected : Boolean = false) {}