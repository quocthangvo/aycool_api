package com.example.shopapp_api.entities.coupon;

public enum DiscountType {

    PERCENT,
    FIXED_AMOUNT;

    public String getDiscountType() {
        switch (this) {
            case PERCENT:
                return "%";
            case FIXED_AMOUNT:
                return "VND";

            default:
                return "Không xác định";
        }
    }

}
