package com.example.shopapp_api.entities.users;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.cart.Cart;
import com.example.shopapp_api.entities.orders.Address;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "nguoi_dung")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_nguoi_dung")
    private int id;

    @Column(name = "ten_tai_khoan", length = 250)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mat_khau", nullable = false)
    private String password;

    @Column(name = "anh_dai_dien")
    private String avatar;

    @Column(name = "trang_thai")
    private boolean active;

    @Column(name = "ngay_sinh")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "ma_quyen")
    private Role role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    @JsonManagedReference
//    private List<Cart> carts;

    /////////////////////////////
    @Override
    //lấy quyền
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName().toUpperCase()));
//        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorityList;

    }

    @Override
    //gia trị nào là user name lấy cái dó đăng nhập
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;
    }
}
