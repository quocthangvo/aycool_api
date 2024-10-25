package com.example.shopapp_api.configurations;

import com.example.shopapp_api.dtos.requests.order.OrderDTO;
import com.example.shopapp_api.entities.orders.Order;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Bỏ qua các trường userId và addressId trong ánh xạ từ OrderDTO -> Order
        modelMapper.addMappings(new PropertyMap<OrderDTO, Order>() {
            @Override
            protected void configure() {
                skip(destination.getId());           // Bỏ qua id (vì sẽ tự động tăng)
                skip(destination.getUser());         // Bỏ qua ánh xạ user (sẽ xử lý thủ công)
                skip(destination.getAddress());      // Bỏ qua ánh xạ address (sẽ xử lý thủ công)
            }
        });

        return modelMapper;
    }
}
