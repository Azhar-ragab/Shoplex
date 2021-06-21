package com.shoplex.shoplex.model.interfaces

interface PaymentListener {
    fun onPaymentComplete()
    fun onPaymentFailedToLoad()
    fun onMinimumPrice()
}