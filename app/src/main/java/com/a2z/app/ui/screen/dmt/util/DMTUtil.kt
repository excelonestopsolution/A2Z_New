package com.a2z.app.ui.screen.dmt.util

object DMTUtil {

    fun dmtTypeToTitle(dmtType: DMTType) = when (dmtType) {
        DMTType.WALLET_1 -> "Wallet 1"
        DMTType.WALLET_2 -> "Wallet 2"
        DMTType.WALLET_3 -> "Wallet 3"
        DMTType.DMT_3 -> "DMT 1"
        DMTType.UPI -> "UPI"
        DMTType.UPI_2 -> "UPI 2"
    }

    fun isUPI(dmtType: DMTType): Boolean {
        return when (dmtType) {
            DMTType.UPI,
            DMTType.UPI_2 -> true
            else -> false
        }
    }
}