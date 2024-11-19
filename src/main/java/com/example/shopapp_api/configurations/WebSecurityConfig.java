package com.example.shopapp_api.configurations;

import com.example.shopapp_api.components.JwtTokenUtil;
import com.example.shopapp_api.entities.users.Role;
import com.example.shopapp_api.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    // lọc kiểm tra đăng nhập có quyền gì để đăng nhập
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)//di qua loc các request can token
                .authorizeHttpRequests(requests -> { // biểu thức lamda ->{}
                    requests.requestMatchers(
                                    String.format("%s/auths/register", apiPrefix),
                                    String.format("%s/auths/login", apiPrefix)
                            )
                            .permitAll() // cho tất cả quyền giống nhau


                            //role
                            .requestMatchers(GET, String.format("%s/roles/**", apiPrefix)).permitAll()
                            //user
                            .requestMatchers(GET, String.format("%s/users/**", apiPrefix)).permitAll()
                            .requestMatchers(POST, String.format("%s/users/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/users/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/users/**", apiPrefix)).hasRole(Role.ADMIN)

                            //category
                            .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                            //sub_category
                            .requestMatchers(POST, String.format("%s/sub_categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/sub_categories/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/sub_categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/sub_categories/**", apiPrefix)).hasRole(Role.ADMIN)


                            //materials
                            .requestMatchers(POST, String.format("%s/materials/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/materials/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/materials/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/materials/**", apiPrefix)).hasRole(Role.ADMIN)

                            //product
                            .requestMatchers(POST, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/products/images/**", apiPrefix)).permitAll()

                            //color
                            .requestMatchers(POST, String.format("%s/colors/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/colors/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/colors/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/colors/**", apiPrefix)).hasRole(Role.ADMIN)

                            //size
                            .requestMatchers(POST, String.format("%s/sizes/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/sizes/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/sizes/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/sizes/**", apiPrefix)).hasRole(Role.ADMIN)

                            //product_details
                            .requestMatchers(POST, String.format("%s/product_details/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/product_details/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/product_details/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/product_details/**", apiPrefix)).hasRole(Role.ADMIN)

                            //prices
                            .requestMatchers(POST, String.format("%s/prices/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(GET, String.format("%s/prices/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/prices/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/prices/**", apiPrefix)).hasRole(Role.ADMIN)

                            //order
                            .requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(GET, String.format("%s/orders/**", apiPrefix)).permitAll()
                            .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            //order_detail
                            .requestMatchers(POST, String.format("%s/order_details/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(GET, String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(DELETE, String.format("%s/order_details/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(PUT, String.format("%s/order_details/**", apiPrefix)).hasRole(Role.USER)
                            //address
                            .requestMatchers(POST, String.format("%s/addresses/**", apiPrefix)).hasAnyRole(Role.USER)
                            .requestMatchers(GET, String.format("%s/addresses/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(DELETE, String.format("%s/addresses/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(PUT, String.format("%s/addresses/**", apiPrefix)).hasRole(Role.USER)

                            //upload ảnh hiển thị
                            .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()

                            .requestMatchers("/uploads/**").permitAll()
                            .anyRequest().authenticated();

                });

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {

                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders((List.of("x-auth-token")));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);

            }
        }); // cho phép tất cả request gửi đến client
//        http.authorizeRequests(authorize -> authorize
//                .requestMatchers("/uploads/**").permitAll() // Cho phép truy cập ảnh mà không cần xác thực
//                .anyRequest().authenticated()// Các endpoint khác yêu cầu xác thực
//        );


        return http.build();
    }
}
