package com.example.shopapp_api.entities.review;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phan_hoi")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma")
    private int id;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String content;


}
