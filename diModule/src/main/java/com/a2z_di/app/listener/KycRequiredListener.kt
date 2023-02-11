package com.a2z_di.app.listener

interface KycRequiredListener {
    fun onAadhaarKycRequired()
    fun onDocumentKycRequired()
}

interface OnSaleItemClickListener{
    fun onSaleItemClick(searchFor : String)
}