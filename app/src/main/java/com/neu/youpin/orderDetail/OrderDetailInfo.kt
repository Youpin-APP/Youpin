package com.neu.youpin.orderDetail

data class OrderDetailInfo(
    val totalPrice : Float, val basic: Basic, val infos : List<Info>, val deliver: Deliver
) {
}

data class Basic (val oid : Int, val stateName : String, val state:Int, val otime1 : String?, val otime2 : String? ,val otime3 : String?)

data class Info (val pic : String, val name : String, val type : String, val gid : Int, val price : Float, val count : Int)

data class Deliver(val tel : String, val name : String, val aid : Int?, val addr : String)