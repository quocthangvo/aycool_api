package com.example.shopapp_api.services.Serv.product;

import com.example.shopapp_api.dtos.requests.product.ProductDTO;
import com.example.shopapp_api.dtos.requests.product.ProductImageDTO;
import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.exceptions.InvalidParamException;
import com.example.shopapp_api.repositories.attribute.MaterialRepository;
import com.example.shopapp_api.repositories.product.ProductImageRepository;
import com.example.shopapp_api.repositories.product.ProductRepository;
import com.example.shopapp_api.repositories.category.SubCategoryRepository;
import com.example.shopapp_api.services.Impl.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final MaterialRepository materialRepository;
    private final ProductImageRepository productImageRepository;

    //final thì @RequiredArgsConstructor thì repository tham chiếu lần 1 và chỉ 1 lần

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        SubCategory existingSubCategory = subCategoryRepository
                .findById(productDTO.getSubCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy danh mục với id: " + productDTO.getSubCategoryId()));

        Material existingMaterial = materialRepository
                .findById(productDTO.getMaterialId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy chất liệu với id: " + productDTO.getMaterialId()));

        // Kiểm tra trùng tên
        if (productRepository.existsByName(productDTO.getName())) {
            throw new RuntimeException("Tên đã tồn tại, vui lòng nhập tên khác.");
        }
        // Kiểm tra trùng mã SKU
        if (productRepository.existsBySku(productDTO.getSku())) {
            throw new RuntimeException("Mã SKU đã tồn tại, vui lòng nhập mã khác.");
        }


        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .description(productDTO.getDescription())
                .subCategory(existingSubCategory)
                .material(existingMaterial)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(int id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(ProductResponse::formProduct);
        // tham chiếu đến phương thức response :: thay vì dùng (product -> )
    }

    @Override
    public void deleteProduct(int id) throws DataNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {// Sử dụng isEmpty() thay cho !isPresent()
            throw new DataNotFoundException(String.format("Không tìm thấy sản phẩm với id = %d", id));
        }

        productRepository.delete(optionalProduct.get());
    }

    @Override
    public Product updateProduct(int id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            //copy các thuộc tình từ DTO ->Product
            //có thể dùng modelMapper
            SubCategory existingSubCategory = subCategoryRepository.findById(productDTO.getSubCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thây danh mục con với id: " +
                            productDTO.getSubCategoryId())); // kiêm tra subCategory có tồn tại id k
            Material existingMaterial = materialRepository.findById(productDTO.getMaterialId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thây chất liệu  với id: " +
                            productDTO.getMaterialId()));
            // Nếu tên mới không thay đổi, chỉ cần cập nhật các thuộc tính khác
            if (!existingProduct.getName().equals(productDTO.getName())) {
                // Kiểm tra trùng mã SKU
                if (productRepository.existsByName(productDTO.getName())) {
                    throw new RuntimeException("Tên đã tồn tại, vui lòng nhập tên khác.");
                }
                // Nếu SKU đã thay đổi, cập nhật SKU mới
                existingProduct.setName(productDTO.getName());
            }

            // Nếu SKU mới không thay đổi, chỉ cần cập nhật các thuộc tính khác
            if (!existingProduct.getSku().equals(productDTO.getSku())) {
                // Kiểm tra trùng mã SKU
                if (productRepository.existsBySku(productDTO.getSku())) {
                    throw new RuntimeException("Mã SKU đã tồn tại, vui lòng nhập mã khác.");
                }
                // Nếu SKU đã thay đổi, cập nhật SKU mới
                existingProduct.setSku(productDTO.getSku());
            }
            //data update
            existingProduct.setName(productDTO.getName());
            existingProduct.setSku(productDTO.getSku());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setSubCategory(existingSubCategory);
            existingProduct.setMaterial(existingMaterial);

            return productRepository.save(existingProduct);

        }
        return null;
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    //upload ảnh
    @Override
    public ProductImage createProductImage(
            int productId,
            ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + productId)); // kiêm tra product có tồn tại id k


        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //ko cho insert quá 5 ảnh cho 1 sp
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Số lượng ảnh tải lên phải nhỏ hơn " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
