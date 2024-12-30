package com.example.shopapp_api.entities.categories;

import com.example.shopapp_api.entities.users.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder //khởi tạo từng thành phần
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "danh_muc_con")

public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_danh_muc_con")
    private int id;

    @Column(name = "ten_danh_muc_con", nullable = false)
    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ma_danh_muc")
    private Category category;
}
