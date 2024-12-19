package com.example.shopapp_api.entities.orders.status;

public enum PaymentMethod {
    COD,
    ONLINE_PAYMENT;

    public String getDisplayPaymentMethod() {
        switch (this) {
            case COD:
                return "Ship COD"; // Giao hàng thanh toán khi nhận hàng
            case ONLINE_PAYMENT:
                return "Thanh toán online"; // Thanh toán trực tuyến
            default:
                throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + this);
        }
    }
}

