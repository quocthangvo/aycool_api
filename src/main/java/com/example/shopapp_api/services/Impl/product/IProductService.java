package com.example.shopapp_api.services.Impl.product;

import com.example.shopapp_api.dtos.requests.product.ProductDTO;
import com.example.shopapp_api.dtos.requests.product.ProductImageDTO;
import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById(int id) throws DataNotFoundException;

    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    void deleteProduct(int id) throws DataNotFoundException;

    Product updateProduct(int id, ProductDTO productDTO) throws DataNotFoundException;

    boolean existsByName(String name);

    ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) throws Exception;
}
