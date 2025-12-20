package com.pht.vntechpc.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class StringFormat {
    companion object {
        fun formatPrice(price: Long): String {
            return "%,d".format(price).replace(",", ".") + " VND"
        }

        fun formatPhoneNumber(phoneNumber: String): String {
            return phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
        }

        fun formatTime(input: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val outputFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")

            val dateTime = LocalDateTime.parse(input, inputFormatter)
            return dateTime.format(outputFormatter)
        }

        fun formatDateTime(input: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val dateTime = LocalDateTime.parse(input, inputFormatter)
            return dateTime.format(outputFormatter)
        }
    }
}