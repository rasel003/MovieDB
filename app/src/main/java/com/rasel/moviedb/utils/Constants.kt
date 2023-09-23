package com.paperflymerchantapp.utils

/**
 * Constants used throughout the app.
 */




const val KEY_ORDER_ID = "order_id"
const val SOURCE = "source"
const val ORDER_TYPE = "orderType"
const val KEY_PHONE_NUMBER = "phone_number"
const val KEY_INVOICE_ID = "invoice_id"
const val USERTYPE = "usertype"
const val KEY_STATUS = "status"

const val LOGIN_REQUEST_CODE = 52145


const val CHANNEL_ID_RECEIVED_LEAVE_REQUEST = "ID_RECEIVED_LEAVE_REQUEST"


enum class OrderStatus(val value: String) {
    IN_TRANSIT("In transit"),
    NOT_YET_PICKED_UP("Not yet picked"),
    ON_THE_WAY_TO_POINT("On the way to point"),
    REACHED_AT_DELIVERY_POINT("Reached at delivery point"),
    ON_THE_WAY_TO_DELIVERY("On the way to delivery"),
    ON_HOLD("On hold"),
}