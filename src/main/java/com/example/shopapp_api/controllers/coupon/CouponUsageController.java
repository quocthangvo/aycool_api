package com.example.shopapp_api.controllers.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/coupon_usages")
@RequiredArgsConstructor
public class CouponUsageController {
}
