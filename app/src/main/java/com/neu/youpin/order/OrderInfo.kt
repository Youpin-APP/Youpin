package com.neu.youpin.order

import java.util.*

data class OrderInfo(val name: String, val pic: Vector<Int>, val price: Int,
                     var type: String) {
}