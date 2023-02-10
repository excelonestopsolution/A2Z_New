package com.di_md.a2z.listener

interface KycRequiredListener {
    fun onAadhaarKycRequired()
    fun onDocumentKycRequired()
}

interface OnSaleItemClickListener{
    fun onSaleItemClick(searchFor : String)
}