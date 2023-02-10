package com.di_md.a2z.data.repository

import com.di_md.a2z.data.service.PaymentGatrewayAndTicket
import javax.inject.Inject

class PaymentgatewayNTicketRepository  @Inject constructor(private val paymentGatrewayAndTicket: PaymentGatrewayAndTicket) {

   suspend fun getOrderAckNumber(number:Long,amount:Long) = paymentGatrewayAndTicket.getOrderAckNumber(
       mobile = number,
       amount = amount
   )

}