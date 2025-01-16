package com.example.shopapp_api.controllers.prices;

import com.example.shopapp_api.dtos.requests.price.PriceDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.price.PriceListResponse;
import com.example.shopapp_api.dtos.responses.price.PriceResponse;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.price.IPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/prices")
@RequiredArgsConstructor
public class PriceController {

    private final IPriceService priceService;

    @PostMapping("")
    public ResponseEntity<?> createPrice(
            @Valid @RequestBody PriceDTO priceDTO,
            BindingResult result) {

        try {
            if (result.hasErrors()) { //kt tích hop data có lỗi k
                List<String> errorMessage = result.getFieldErrors()//trả về ds doi tuong FieldError
                        .stream()// chuyen FieldError thành stream cho phép xu ly chuoi thao tac
                        .map(FieldError::getDefaultMessage) //chuyển FieldError thành chuỗi thông báo lỗi (message).
                        .toList();//Stream thành danh sách các chuỗi thông báo lỗi
                return ResponseEntity.badRequest().body(errorMessage);
            }// xử lý validation

            Price createPrice = priceService.createPrice(priceDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", createPrice));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/product_detail/all/{product_detail_id}")
    public ResponseEntity<ApiResponse<List<?>>> getALlPriceByProductDetailId(@PathVariable("product_detail_id") int productDetailId
    ) {
        try {
            List<Price> priceList = priceService.getAllPriceByProductDetailId(productDetailId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", priceList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }

    @GetMapping("/product_detail/{product_detail_id}")
    public ResponseEntity<ApiResponse<?>> getPriceByProductDetailId(@PathVariable("product_detail_id") int productDetailId
    ) {
        try {
            PriceResponse priceList = priceService.getPriceByProductDetailId(productDetailId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", priceList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPriceId(@PathVariable("id") int priceId) {
        try {
            Price existingPrice = priceService.getPriceById(priceId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingPrice));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletePrice(@PathVariable("id") int id) {
        try {
            priceService.deletePrice(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa giá có id = %d thành công", id)));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePrice(
            @PathVariable int id,
            @Valid @RequestBody PriceDTO priceDTO) {
        try {
            PriceResponse updatePrice = priceService.updatePrice(id, priceDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updatePrice));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

//    @GetMapping("")
//    public ResponseEntity<ApiResponse<?>> getAllPrices(
//            @RequestParam("page") int page,
//            @RequestParam("limit") int limit,
//            @RequestParam(value = "sortOrder", defaultValue = "") String sortOrder
//    ) {
//        try {
//            // Điều chỉnh sắp xếp theo yêu cầu của người dùng
////            Sort sort = sortOrder.equals("asc") ?
////                    Sort.by(Sort.Order.asc("sellingPrice")) :
////                    Sort.by(Sort.Order.desc("sellingPrice"));
//            Sort sort;
//
//            // Nếu sortOrder không có giá trị, sắp xếp theo createdAt mặc định
//            if (sortOrder.isEmpty()) {
//                sort = Sort.by(Sort.Order.desc("createdAt"));  // Sort by startDate in descending order
//            } else if (sortOrder.equals("asc")) {
//                sort = Sort.by(Sort.Order.asc("sellingPrice"));
//            } else {
//                sort = Sort.by(Sort.Order.desc("sellingPrice"));
//            }
//
//
//            PageRequest pageRequest = PageRequest.of(page, limit, sort);
//
//            // Lấy dữ liệu phân trang từ service
//            Page<PriceResponse> pricePage = priceService.getAllPrices(pageRequest);
//
//            // Tính toán các thông tin phân trang
//            int totalPages = pricePage.getTotalPages();    // Tổng số trang
//            long totalRecords = pricePage.getTotalElements(); // Tổng số bản ghi
//            List<PriceResponse> priceList = pricePage.getContent(); // Danh sách giá
//
//            // Tạo PriceListResponse để trả về
//            PriceListResponse priceListResponse = PriceListResponse.builder()
//                    .priceResponseList(priceList)
//                    .totalPages(totalPages)
//                    .totalRecords(totalRecords)
//                    .build();
//
//            return ResponseEntity.ok(new ApiResponse<>("Thành công", priceListResponse));
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
//        }
//    }


    @GetMapping("")
    public ResponseEntity<ApiResponse<PriceListResponse>> getAllPrices(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(required = false) String sort,
            @RequestParam(value = "productDetailName", required = false) String productDetailName) {

        PageRequest pageRequest;

        if (sort != null) {
            Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequest = PageRequest.of(page, limit, Sort.by(sortDirection, "sellingPrice"));
        } else {
            // Sắp xếp theo createdAt mới nhất khi sort không được cung cấp
            pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        Page<PriceResponse> pricePage;

        if (productDetailName != null && !productDetailName.isEmpty()) {
            pricePage = priceService.findPricesByProductDetailNameContaining(pageRequest, productDetailName);
        } else {
            pricePage = priceService.getAllPrices(pageRequest);  // Trả về tất cả sản phẩm nếu không có điều kiện tìm kiếm
        }

        // Tính toán các thông tin phân trang
        int totalPages = pricePage.getTotalPages();    // Tổng số trang
        long totalRecords = pricePage.getTotalElements(); // Tổng số bản ghi
        List<PriceResponse> priceList = pricePage.getContent(); // Danh sách giá

        // Tạo PriceListResponse để trả về
        PriceListResponse priceListResponse = PriceListResponse.builder()
                .priceResponseList(priceList)
                .totalPages(totalPages)
                .totalRecords(totalRecords)
                .build();

        return ResponseEntity.ok(new ApiResponse<>("Thành công", priceListResponse));
    }


}
