package com.a2z.app.dist.data.repository

import com.a2z.app.dist.data.service.PaymentGatrewayAndTicket
import javax.inject.Inject

class PaymentgatewayNTicketRepository  @Inject constructor(private val paymentGatrewayAndTicket: PaymentGatrewayAndTicket) {

   suspend fun getOrderAckNumber(number:Long,amount:Long) = paymentGatrewayAndTicket.getOrderAckNumber(
       mobile = number,
       amount = amount
   )

}