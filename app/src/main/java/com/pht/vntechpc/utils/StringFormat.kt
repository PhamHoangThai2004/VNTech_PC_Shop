package com.pht.vntechpc.utils

fun formatPrice(price: Long): String {
    return "%,d".format(price).replace(",", ".") + " VND"
}

fun formatPhoneNumber(phoneNumber: String): String {
    return phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
}