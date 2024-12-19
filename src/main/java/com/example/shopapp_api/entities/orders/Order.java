package com.example.shopapp_api.entities.orders;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.entities.orders.status.PaymentMethod;
import com.example.shopapp_api.entities.orders.status.PaymentStatus;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @JoinColumn(name = "order_code")
    private String orderCode;

    @Column(name = "note", length = 250)
    private String note;

    @Column(name = "order_date")
    private LocalDateTime OrderDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;

    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
