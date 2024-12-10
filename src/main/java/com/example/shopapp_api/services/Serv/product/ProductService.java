package com.example.shopapp_api.services.Serv.product;

import com.example.shopapp_api.dtos.requests.product.ProductDTO;
import com.example.shopapp_api.dtos.requests.product.ProductImageDTO;
import com.example.shopapp_api.dtos.responses.product.products.ProductImageResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductSelectResponse;
import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.exceptions.InvalidParamException;
import com.example.shopapp_api.repositories.attribute.ColorRepository;
import com.example.shopapp_api.repositories.attribute.MaterialRepository;
import com.example.shopapp_api.repositories.attribute.SizeRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.product.ProductImageRepository;
import com.example.shopapp_api.repositories.product.ProductRepository;
import com.example.shopapp_api.repositories.category.SubCategoryRepository;
import com.example.shopapp_api.services.Impl.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@RestController

public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final MaterialRepository materialRepository;
    private final ProductImageRepository productImageRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ProductDetailRepository productDetailRepository;


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

        newProduct = productRepository.save(newProduct);

        String baseSku = newProduct.getSku();
        int skuCounter = 1;

        //duyệt qua vòng lập để truyền vào list tạo size color
        for (Integer color : productDTO.getColorId()) {
            for (Integer size : productDTO.getSizeId()) {
                Color existingColor = colorRepository.findById(color)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy màu" + productDTO.getColorId()));
                Size existingSize = sizeRepository.findById(size)
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy kích thước" + productDTO.getSizeId()));

                String skuVersion = baseSku + String.format("%03d", skuCounter++);

                String skuName = newProduct.getName() + "-" + existingColor.getName() + "-" + existingSize.getName();

                ProductDetail newProductDetail =
                        ProductDetail.builder()
                                .product(newProduct)
                                .color(existingColor)
                                .size(existingSize)
                                .skuVersion(skuVersion)
                                .skuName(skuName)
                                .build();
                productDetailRepository.save(newProductDetail);
            }
        }
        return newProduct;
    }

    @Override
    public ProductResponse getProductById(int id) throws DataNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
        return ProductResponse.formProduct(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(ProductResponse::formProduct);
        // tham chiếu đến phương thức response :: thay vì dùng (product -> )
    }

    @Transactional
    @Override
    public void deleteProduct(int id) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
        if (productDetailRepository.existsByProductId(id)) {
            throw new DataNotFoundException("Không thể xóa sản phẩm vì còn tồn tại chi tiết sản phẩm liên quan.");
        }

        productRepository.delete(existingProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(int id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
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


    @Override
    public List<ProductImageResponse> getImageByProductId(int id) throws DataNotFoundException {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
        // Kiểm tra nếu productId tồn tại trong cơ sở dữ liệu
        if (!productRepository.existsById(id)) {
            throw new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id);
        }
        List<ProductImage> images = productImageRepository.findByProductId(id);
        if (images.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy ảnh nào cho sản phẩm với id: " + id);
        }

        return images.stream()
                .map(ProductImageResponse::formProductImage) // Gọi phương thức chuyển đổi
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductSelectResponse> getAllProductsNotPage() {
        // Fetch all products from the repository
        List<Product> products = productRepository.findAll();

        // Convert each product to a ProductResponse
        return products.stream()
                .sorted((product1, product2) -> product2.getCreatedAt().compareTo(product1.getCreatedAt()))
                .map(ProductSelectResponse::formProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponse> searchProducts(String name, Integer materialId, PageRequest pageRequest) {
        return productRepository
                .searchByNameAndMaterial(name, materialId, pageRequest)
                .map(ProductResponse::formProduct); // Chuyển đổi từ Product sang ProductResponse
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(int categoryId, Pageable pageable) {
        // Fetch paginated products by category
        Page<Product> productsPage = productRepository.findBySubCategoryCategoryId(categoryId, pageable);

        // Convert the Page<Product> into Page<ProductResponse>
        return productsPage.map(ProductResponse::formProduct);
    }


    @Override
// Lấy danh sách sản phẩm theo subcategory với phân trang
    public Page<ProductResponse> getProductsBySubCategory(int subCategoryId,
                                                          Integer colorId,
                                                          List<Integer> sizeIds,
                                                          List<Integer> materialIds,
                                                          Pageable pageable) {
        // Gọi phương thức từ ProductRepository với các tham số lọc
        Page<Product> products = productRepository
                .findProductsByFilters(subCategoryId, colorId, sizeIds, materialIds, pageable);

        // Chuyển đổi Page<Product> thành Page<ProductResponse>
        return products.map(ProductResponse::formProduct);
    }


    @Override
    public Page<ProductResponse> searchProductsByNameAndSubCategory(String name, Integer subCategoryId, Pageable pageable) {
        Page<Product> productPage;

        // Kiểm tra nếu có cả tên và subCategoryId
        if (name != null && subCategoryId != null) {
            productPage = productRepository.findByNameContainingAndSubCategoryId(name, subCategoryId, pageable);
        }
        // Kiểm tra nếu chỉ có tên
        else if (name != null) {
            productPage = productRepository.findByNameContaining(name, pageable);
        }
        // Kiểm tra nếu chỉ có subCategoryId
        else if (subCategoryId != null) {
            productPage = productRepository.findBySubCategoryId(subCategoryId, pageable);
        }
        // Nếu không có điều kiện lọc nào, lấy tất cả sản phẩm với sắp xếp
        else {
            productPage = productRepository.findAll(pageable);
        }

        // Chuyển từ Page<Product> sang Page<ProductResponse>
        return productPage.map(ProductResponse::formProduct);
    }


}
