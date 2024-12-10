package com.example.shopapp_api.entities.orders;

public enum PaymentStatus {

    NOPAYMENT,
    PAID;

    public String getStatusDisplayPayment() {
        // Sử dụng switch đơn giản để trả về trạng thái hiển thị
        switch (this) {
            case NOPAYMENT:
                return "Chưa thanh toán";
            case PAID:
                return "Đã thanh toán";
            default:
                return "Trạng thái không hợp lệ"; // Trường hợp mặc định, nếu enum có thêm trạng thái mới
        }
    }
//    PENDING,   // Chưa thanh toán
//    PAID,      // Đã thanh toán
//    NOPAYMENT
}
