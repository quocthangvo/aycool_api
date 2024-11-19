package com.example.shopapp_api.controllers.products;

import com.example.shopapp_api.dtos.requests.product.ProductDTO;
import com.example.shopapp_api.dtos.requests.product.ProductImageDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.product.ProductImageResponse;
import com.example.shopapp_api.dtos.responses.product.ProductListResponse;
import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import com.example.shopapp_api.dtos.responses.product.ProductSelectResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.product.IProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final IProductService productService;

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
            List<ProductResponse> productList = productPage.getContent();//từ productPgae lấy ra ds các product getContent

            ProductListResponse productListResponse = (ProductListResponse
                    .builder()
                    .productResponseList(productList)
                    .totalPages(totalPages)
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

//    @GetMapping("/images/{imageName}")
//    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
//        try {
//            Path imagePath = Paths.get("uploads/" + imageName);
//            UrlResource resource = new UrlResource(imagePath.toUri());
//            if (resource.exists()) {
////                // Kiểm tra định dạng của file (ví dụ PNG hoặc JPG)
////                MediaType mediaType = MediaType.IMAGE_PNG; // Mặc định là PNG
////                String contentType = Files.probeContentType(imagePath);
////                if (contentType != null && contentType.startsWith("image")) {
////                    mediaType = MediaType.parseMediaType(contentType);
////                }
//
//                // Trả về file với mediaType tương ứng
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_JPEG)  // Đảm bảo trả về đúng loại media
//                        .body(resource);
//            } else {
////                return ResponseEntity.status(HttpStatus.NOT_FOUND)
////                        .body(new ApiResponse<>("Ảnh không tìm thấy", null));
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
////            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProductSelectResponse>>> getAllProducts() {
        List<ProductSelectResponse> products = productService.getAllProductsNotPage();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", products));
    }


    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) {
        try {
            // Đường dẫn tới file hình ảnh trong thư mục uploads
            Path duongDanHinhAnh = Paths.get("uploads").resolve(imageName).normalize();

            // Kiểm tra xem file có tồn tại và có thể đọc được không
            if (!Files.exists(duongDanHinhAnh) || !Files.isReadable(duongDanHinhAnh)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy hình ảnh hoặc không thể truy cập.");
            }

            // Lấy loại MIME (loại nội dung) của file
            String loaiNoiDung = Files.probeContentType(duongDanHinhAnh);
            if (loaiNoiDung == null || !loaiNoiDung.startsWith("image")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File không phải là loại hình ảnh hợp lệ.");
            }

            // Tạo resource từ file
            UrlResource resource = new UrlResource(duongDanHinhAnh.toUri());

            // Kiểm tra lần cuối nếu resource tồn tại
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(loaiNoiDung)) // Đặt đúng loại MIME
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy hình ảnh.");
            }
        } catch (Exception e) {
            // Xử lý lỗi và trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tải hình ảnh: " + e.getMessage());
        }
    }

}
