package com.example.shopapp_api.entities.orders.status;


public enum OrderStatus {


    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public String getStatusDisplayName() {
        switch (this) {
            case PENDING:
                return "Chờ xử lý";
            case PROCESSING:
                return "Đang xử lý";
            case SHIPPED:
                return "Đang giao hàng";
            case DELIVERED:
                return "Đã giao hàng";
            case CANCELLED:
                return "Đã huỷ";
            default:
                return "Không xác định";
        }
    }
}

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