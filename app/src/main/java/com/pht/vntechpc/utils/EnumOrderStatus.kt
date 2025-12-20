package com.pht.vntechpc.utils

import androidx.compose.ui.graphics.Color
import com.pht.vntechpc.ui.theme.Active
import com.pht.vntechpc.ui.theme.Inactive
import com.pht.vntechpc.ui.theme.Info

class EnumOrderStatus {
    companion object {
        const val PENDING = "PENDING"
        const val CONFIRMED = "CONFIRMED"
        const val PROCESSING = "PROCESSING"
        const val SHIPPING = "SHIPPING"
        const val DELIVERED = "DELIVERED"
        const val CANCELLED = "CANCELLED"
        const val RETURNED = "RETURNED"
        const val REFUNDED = "REFUNDED"

        fun toStatus(status: String): String {
            return when (status) {
                PENDING -> "Chờ xác nhận"
                CONFIRMED -> "Đã xác nhận"
                PROCESSING -> "Đang xử lý"
                SHIPPING -> "Đang giao hàng"
                DELIVERED -> "Đã giao thành công"
                CANCELLED -> "Đã hủy"
                RETURNED -> "Đã hoàn hàng"
                REFUNDED -> "Đã hoàn tiền"
                else -> "Không hợp lệ"
            }
        }

        fun toTitle(status: String): String {
            return when (status) {
                PENDING -> "Đơn hàng đang chờ xác nhận"
                CONFIRMED -> "Đơn hàng đã được xác nhận"
                PROCESSING -> "Đơn hàng đang xử lý"
                SHIPPING -> "Đơn hàng đang được giao"
                DELIVERED -> "Đã hoàn tất đơn hàng"
                CANCELLED -> "Đơn hàng đã bị hủy"
                RETURNED -> "Đơn hàng đã hoàn trả"
                REFUNDED -> "Đơn hàng đã được hoàn tiền"
                else -> "Chi tiết đơn hàng"
            }
        }

        fun toOrderStatusInfo(status: String, date: String): String {
            val dateInfo = StringFormat.formatTime(date)
            return when (status) {
                PENDING -> "Đơn hàng của bạn đang chờ xác nhận!"
                CONFIRMED -> "Đơn hàng của bạn đã được xác nhận lúc $dateInfo"
                PROCESSING -> "Đơn hàng đang xử lý!"
                SHIPPING -> "Đơn hàng của bạn đang được giao!"
                DELIVERED -> "Đơn hàng của bạn đã được giao vào lúc $dateInfo"
                CANCELLED -> "Đơn hàng của bạn đã bị hủy vào lúc $dateInfo"
                RETURNED -> "Bạn đã hoàn hàng đơn hàng!"
                REFUNDED -> "Đơn hàng đã được hoàn tiền!"
                else -> "Trạng thái không hợp lệ!"
            }
        }

        fun toColor(status: String): Color {
            return when (status) {
                CANCELLED, RETURNED -> Inactive
                DELIVERED -> Active
                else -> Info
            }
        }
    }
}