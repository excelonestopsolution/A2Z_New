package com.a2z_di.app.data.repository

import com.a2z_di.app.data.service.PaymentGatrewayAndTicket
import javax.inject.Inject

class PaymentgatewayNTicketRepository  @Inject constructor(private val paymentGatrewayAndTicket: PaymentGatrewayAndTicket) {

   suspend fun getOrderAckNumber(number:Long,amount:Long) = paymentGatrewayAndTicket.getOrderAckNumber(
       mobile = number,
       amount = amount
   )

}