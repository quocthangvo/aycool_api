package com.example.shopapp_api.services.Serv.product;

import com.example.shopapp_api.dtos.requests.product.ProductDetailDTO;
import com.example.shopapp_api.dtos.requests.product.UpdateProductDetailDTO;
import com.example.shopapp_api.dtos.responses.product.ProductDetailResponse;
import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.attribute.ColorRepository;
import com.example.shopapp_api.repositories.attribute.SizeRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.product.ProductRepository;
import com.example.shopapp_api.services.Impl.product.IProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDetailService implements IProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    @Override
    public List<ProductDetail> createProductDetail(ProductDetailDTO productDetailDTO) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(productDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy danh mục sản phẩm với id: " + productDetailDTO.getProductId()));

        // Xử lý màu sắc
        List<Color> colors = new ArrayList<>();
        for (Integer colorId : productDetailDTO.getColorId()) {
            Color existingColor = colorRepository.findById(colorId)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy màu sắc với id: " + colorId));
            colors.add(existingColor);
        }
        // Xử lý kích thước
        List<Size> sizes = new ArrayList<>();
        for (Integer sizeId : productDetailDTO.getSizeId()) {
            Size existingSize = sizeRepository.findById(sizeId)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy kích thước với id: " + sizeId));
            sizes.add(existingSize);
        }
        // Lấy mã SKU từ sản phẩm
        String baseSku = existingProduct.getSku();
        int skuCounter = 1;

        List<ProductDetail> productDetails = new ArrayList<>();
        for (Color color : colors) {
            for (Size size : sizes) {
                // Kiểm tra xem ProductDetail đã tồn tại chưa
                Optional<ProductDetail> existingProductDetail = productDetailRepository
                        .findByProductAndColorAndSize(existingProduct, color, size);
                if (existingProductDetail.isPresent()) {
                    throw new DataNotFoundException("Không thể tạo chi tiết sản phẩm đã tồn tại, có màu "
                            + color.getName() + " kích thước " + size.getName());
                } else {
                    //tạo mã sku tự động
                    String skuVersion = baseSku + String.format("%03d", skuCounter++);
                    ProductDetail productDetail = ProductDetail.builder()
                            .skuVersion(skuVersion)
                            .quantity(productDetailDTO.getQuantity())  // Nếu bạn muốn mỗi chi tiết sản phẩm có số lượng riêng, hãy điều chỉnh tại đây
                            .product(existingProduct)
                            .color(color)
                            .size(size)
                            .build();
                    // Lưu ProductDetail
                    productDetails.add(productDetailRepository.save(productDetail));
                }
            }
        }

        return productDetails;

//        ProductDetail productDetail = ProductDetail.builder()
//                .skuVersion(productDetailDTO.getSkuVersion())
//                .quantity(productDetailDTO.getQuantity())
//                .product(existingProduct)
//                .color((Color) colors)
//                .size((Size) sizes)
//                .build();
//        return productDetailRepository.save(productDetail);
    }

    @Override
    public ProductDetailResponse getProductDetailById(int id) throws DataNotFoundException {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
        return ProductDetailResponse.formProductDetail(productDetail);
    }

    @Override
    public List<ProductDetailResponse> getAllProductDetailsByProductId(int productId) {
        List<ProductDetail> productDetails = productDetailRepository.findByProductId(productId);
        return productDetails.stream()
                .map(ProductDetailResponse::formProductDetail)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductDetail(int id) throws DataNotFoundException {
        getProductDetailById(id);
        productDetailRepository.deleteById(id);
    }

    @Override
    public ProductDetail updateProductDetail(int id, UpdateProductDetailDTO updateProductDetailDTO) throws DataNotFoundException {
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
        Color existingColor = colorRepository.findById(updateProductDetailDTO.getColorId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy màu sắc với id: " + updateProductDetailDTO.getColorId()));
        Size existingSize = sizeRepository.findById(updateProductDetailDTO.getSizeId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy kích thước với id: " + updateProductDetailDTO.getSizeId()));

        productDetail.setSkuVersion(updateProductDetailDTO.getSkuVersion());
        productDetail.setQuantity(updateProductDetailDTO.getQuantity());
        productDetail.setColor(existingColor);
        productDetail.setSize(existingSize);

        return productDetailRepository.save(productDetail);

    }

    @Override
    public List<ProductDetailResponse> getAllProductDetails() {
        List<ProductDetail> productDetails = productDetailRepository.findAllByOrderByIdDesc();
        return productDetails.stream()
                .map(ProductDetailResponse::formProductDetail)
                .collect(Collectors.toList());
    }

}
