package com.example.shopapp_api.controllers.products;

import com.example.shopapp_api.dtos.requests.product.ProductDTO;
import com.example.shopapp_api.dtos.requests.product.ProductImageDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductImageResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductListResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.product.ProductImageRepository;
import com.example.shopapp_api.services.Impl.product.IProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:4201"}
)
public class ProductController {
    private final IProductService productService;
    private final ProductImageRepository productImageRepository;

    @GetMapping("")
    public ResponseEntity<?> getALlProducts(
            //truyền productListResponse thì k cần List<>
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        try {
            //tạo PagesRequest từ thông tin page và limit
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());

            Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
            //tông số trang
            int totalPages = productPage.getTotalPages();//lấy ra tổng số trang
            long totalRecords = productPage.getTotalElements();
            List<ProductResponse> productList = productPage.getContent();//từ productPgae lấy ra ds các product getContent

            ProductListResponse productListResponse = (ProductListResponse
                    .builder()
                    .productResponseList(productList)
                    .totalPages(totalPages)
                    .totalRecords(totalRecords)
                    .build());
            return ResponseEntity.ok(new ApiResponse<>("Thành công", productListResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result) {

        try {
            if (result.hasErrors()) { //kt tích hop data có lỗi k
                List<String> errorMessage = result.getFieldErrors()//trả về ds doi tuong FieldError
                        .stream()// chuyen FieldError thành stream cho phép xu ly chuoi thao tac
                        .map(FieldError::getDefaultMessage) //chuyển FieldError thành chuỗi thông báo lỗi (message).
                        .toList();//Stream thành danh sách các chuỗi thông báo lỗi
                return ResponseEntity.badRequest().body(errorMessage);
            }// xử lý validation

            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", newProduct));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductId(@PathVariable("id") int productId) {
        try {
            ProductResponse existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }

    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa sản phẩm có id = %d thành công", id)));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductDTO productDTO) {
        try {
            Product updateProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") int productId,
            @RequestParam("files") List<MultipartFile> files) {
        try {

            ProductResponse existingProduct = productService.getProductById(productId);

            files = files == null ? new ArrayList<MultipartFile>() : files;

            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }

            // **Delete old images before uploading new ones**
            List<ProductImage> existingImages = productImageRepository.findByProductId(productId);
            for (ProductImage image : existingImages) {
                deleteFile(image.getImageUrl()); // Delete the old image file from the storage
                productImageRepository.delete(image); // Delete the image record from the database
            }

            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue; // ko chọn ảnh vẫn tiếp tục
                }

                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large? Maximum size is 10MB");
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }

                String filename = storeFile(file);
                //lưu đối tượng product trong db
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    private void deleteFile(String filename) throws IOException {
        Path uploadDir = Paths.get("uploads");
        Path filePath = uploadDir.resolve(filename);
        Files.deleteIfExists(filePath); // Delete the file if it exists
    }

    /////////////////upload images
    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //thêm UUID vào trươớc tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //đường dẫn dến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        //kiểm tra và tạo thư mục nếu nó k tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        //REPLACE_EXISTING có image moi thay the
        return uniqueFilename;
    }

    //kt đúng file ảnh k
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }


    @GetMapping("/images/{id}")
    public ResponseEntity<?> getImageByProductId(@PathVariable("id") int productId) {
        try {
            List<ProductImageResponse> existingProduct = productService.getImageByProductId(productId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
//                // Kiểm tra định dạng của file (ví dụ PNG hoặc JPG)
//                MediaType mediaType = MediaType.IMAGE_PNG; // Mặc định là PNG
//                String contentType = Files.probeContentType(imagePath);
//                if (contentType != null && contentType.startsWith("image")) {
//                    mediaType = MediaType.parseMediaType(contentType);
//                }

                // Trả về file với mediaType tương ứng
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)  // Đảm bảo trả về đúng loại media
                        .body(resource);
            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse<>("Ảnh không tìm thấy", null));
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<ProductListResponse>> getAllProductss(
            @RequestParam("page") int page,          // Mặc định là trang đầu tiên
            @RequestParam("limit") int limit,       // Mặc định là 10 sản phẩm mỗi trang
            @RequestParam(required = false) String name,        // Tìm kiếm theo tên
            @RequestParam(required = false) Integer materialId  // Lọc theo chất liệu
    ) {
        // Tạo PageRequest với sắp xếp theo "createdAt" giảm dần
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        // Gọi service để lấy dữ liệu sản phẩm
        Page<ProductResponse> productPage;
        if (name != null || materialId != null) {
            // Nếu có tìm kiếm hoặc lọc, sử dụng phương thức search
            productPage = productService.searchProducts(name, materialId, pageRequest);
        } else {
            // Nếu không có tìm kiếm hoặc lọc, lấy tất cả sản phẩm
            productPage = productService.getAllProducts(pageRequest);
        }

        // Tính toán các thông tin phân trang
        int totalPages = productPage.getTotalPages();    // Tổng số trang
        long totalRecords = productPage.getTotalElements(); // Tổng số bản ghi
        List<ProductResponse> productList = productPage.getContent(); // Danh sách sản phẩm

        // Tạo ProductListResponse để trả về
        ProductListResponse productListResponse = ProductListResponse.builder()
                .productResponseList(productList)
                .totalPages(totalPages)
                .totalRecords(totalRecords)
                .build();

        // Đóng gói và trả về ApiResponse
        ApiResponse<ProductListResponse> response = new ApiResponse<>("Thành công", productListResponse);
        return ResponseEntity.ok(response);
    }

    // API để lấy sản phẩm theo category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<ProductListResponse>> getProductsByCategory(
            @PathVariable int categoryId,
            @RequestParam("page") int page,          // Mặc định là trang đầu tiên
            @RequestParam("limit") int limit) {  // Số lượng sản phẩm mỗi trang, mặc định là 10


        Sort sort = Sort.by("createdAt").descending();
        // Tạo Pageable từ tham số page và size
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<ProductResponse> productPage = productService.getProductsByCategory(categoryId, pageable);

        int totalPages = productPage.getTotalPages();
        long totalRecords = productPage.getTotalElements();
        List<ProductResponse> productList = productPage.getContent();

        ProductListResponse productListResponse = ProductListResponse.builder()
                .productResponseList(productList)
                .totalPages(totalPages)
                .totalRecords(totalRecords)
                .build();

        ApiResponse<ProductListResponse> response = new ApiResponse<>("Thành công", productListResponse);
        return ResponseEntity.ok(response);
    }


    // API để lấy sản phẩm theo subcategory
    @GetMapping("/sub_category/{subCategoryId}")
    public ResponseEntity<ApiResponse<ProductListResponse>> getProductsBySubCategory(
            @PathVariable int subCategoryId,
            @RequestParam(value = "color_id", required = false) Integer colorId,
            @RequestParam(value = "size_ids", required = false) List<Integer> sizeIds,
            @RequestParam(value = "material_ids", required = false) List<Integer> materialIds,
            @RequestParam(value = "page") int page,          // Mặc định là trang đầu tiên
            @RequestParam(value = "limit") int limit
    ) {

        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService
                .getProductsBySubCategory(subCategoryId, colorId, sizeIds, materialIds, pageable);


        int totalPages = productPage.getTotalPages();    // Tổng số trang
        long totalRecords = productPage.getTotalElements(); // Tổng số bản ghi
        List<ProductResponse> productList = productPage.getContent(); // Danh sách sản phẩm


        // Tạo ProductListResponse để trả về
        ProductListResponse productListResponse = ProductListResponse.builder()
                .productResponseList(productList)
                .totalPages(totalPages)
                .totalRecords(totalRecords)
                .build();
        ApiResponse<ProductListResponse> response = new ApiResponse<>("Thành công", productListResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ProductListResponse>> searchProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sub_category_id", required = false) Integer subCategoryId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {


        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.searchProductsByNameAndSubCategory(name, subCategoryId, pageable);

        int totalPages = productPage.getTotalPages();
        long totalRecords = productPage.getTotalElements();
        List<ProductResponse> productList = productPage.getContent();

        ProductListResponse productListResponse = ProductListResponse.builder()
                .productResponseList(productList)
                .totalPages(totalPages)
                .totalRecords(totalRecords)
                .build();

        ApiResponse<ProductListResponse> response = new ApiResponse<>("Thành công", productListResponse);
        return ResponseEntity.ok(response);
    }


}
