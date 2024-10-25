package com.example.shopapp_api.entities.orders;


public enum OrderStatus {
    //    PENDING("Chờ xử lý"),
//    PROCESSING("Đã xác nhận"),
//    SHIPPED("Đang giao hàng"),
//    DELIVERED("Đã giao hàng"),
//    CANCELLED("Hủy");
//

//    private final String displayName;
//
//    OrderStatus(String displayName) {
//        this.displayName = displayName;
//    }
//
//    public String getDisplayName() {
//        return displayName;
//    }

    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;
}

