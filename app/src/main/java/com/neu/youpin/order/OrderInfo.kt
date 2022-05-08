package com.neu.youpin.order

import java.util.*

data class OrderInfo(val name: String, val pics: Vector<String>, val totalPrice: Float,
                     var stateName: String, val oid: Int) {
}