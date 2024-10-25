package com.example.shopapp_api.services.Impl.product;

import com.example.shopapp_api.dtos.requests.product.ProductDetailDTO;
import com.example.shopapp_api.dtos.requests.product.UpdateProductDetailDTO;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;


import java.util.List;

public interface IProductDetailService {
    List<ProductDetail> createProductDetail(ProductDetailDTO productDetailDTO) throws DataNotFoundException;

    ProductDetail getProductDetailById(int id) throws DataNotFoundException;

    List<ProductDetail> getAllProductDetailsByProductId(int productId);

    void deleteProductDetail(int id) throws DataNotFoundException;

    ProductDetail updateProductDetail(int id, UpdateProductDetailDTO updateProductDetailDTO) throws DataNotFoundException;
}
