package com.example.shopapp_api.services.Serv.price;

import com.example.shopapp_api.dtos.requests.price.PriceDTO;
import com.example.shopapp_api.dtos.responses.price.PriceResponse;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.price.PriceRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.services.Impl.price.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceService implements IPriceService {
    private final PriceRepository priceRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public Price createPrice(PriceDTO priceDTO) throws DataNotFoundException {
        ProductDetail existingProductDetail = productDetailRepository
                .findById(priceDTO.getProductDetailId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy sản phẩm với id: " + priceDTO.getProductDetailId()));

        // Kiểm tra ngày bắt đầu không nhỏ hơn hôm nay
        if (priceDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Ngày bắt đầu không thể nhỏ hơn hôm nay.");
        }

        // Kiểm tra ngày kết thúc phải lớn hơn và khác ngày bắt đầu, nếu có
        if (priceDTO.getEndDate() != null && !priceDTO.getEndDate().isAfter(priceDTO.getStartDate())) {
            throw new DataNotFoundException("Ngày kết thúc phải lớn hơn và khác ngày bắt đầu.");
        }

        // Tính giá khuyến mãi dựa trên tỷ lệ phần trăm nếu có
        Float promotionPrice = priceDTO.getPromotionPrice();
        if (promotionPrice != null && promotionPrice < 0) {
            throw new IllegalArgumentException("Không thể nhập % là âm.");
        }
        Float calculatedPromotionPrice = null;
        if (promotionPrice != null && promotionPrice >= 0) {
            // Tính giá khuyến mãi bằng cách áp dụng phần trăm khuyến mãi
            calculatedPromotionPrice = priceDTO.getSellingPrice() * (1 - promotionPrice / 100);
        }

        // Kiểm tra và cập nhật giá cũ (nếu có) để giá này hết hiệu lực
        if (priceDTO.getEndDate() == null) {
            // Nếu không có ngày kết thúc, không làm gì cả
        } else {
            // Nếu có ngày kết thúc, cần cập nhật giá cũ nếu giá này vẫn chưa hết hạn
            Price existingPrice = priceRepository
                    .findFirstByProductDetailIdAndEndDateIsNull(existingProductDetail.getId());

            if (existingPrice != null) {
                // Cập nhật giá cũ để giá này hết hiệu lực
                existingPrice.setEndDate(LocalDate.now());
                priceRepository.save(existingPrice); // Cập nhật giá cũ
            }
        }


        Price createPrice = Price.builder()
                .sellingPrice(priceDTO.getSellingPrice())
                .promotionPrice(calculatedPromotionPrice)
                .startDate(priceDTO.getStartDate())
                .endDate(priceDTO.getEndDate())
                .productDetail(existingProductDetail)
                .build();

        return priceRepository.save(createPrice);
    }

    @Override
    public Price getPriceById(int id) throws DataNotFoundException {
        return priceRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá với id: " + id));
    }

    @Override
    public List<PriceResponse> getAllPrices() {
        List<Price> prices = priceRepository.findAllByOrderByCreatedAtDesc(); // Lấy tất cả giá sắp xếp theo ngày bắt đầu

        // Lọc ra giá mới nhất cho mỗi productDetailId
        Map<Integer, Price> latestPrices = new HashMap<>();
        for (Price price : prices) {
            int productDetailId = price.getProductDetail().getId();
            if (!latestPrices.containsKey(productDetailId)) {
                latestPrices.put(productDetailId, price);
            }
        }

        // Chuyển đổi thành danh sách PriceResponse
        List<PriceResponse> priceResponses = latestPrices.values().stream()
                .map(PriceResponse::formPrice)
                .collect(Collectors.toList());

        return priceResponses;
    }


    @Override
    public List<Price> getAllPriceByProductDetailId(int productDetailId) {
        return priceRepository.findByProductDetailId(productDetailId);
    }

    @Override
    public PriceResponse getPriceByProductDetailId(int productDetailId) {
        Price price = priceRepository.findTopByProductDetailIdOrderByCreatedAtDesc(productDetailId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giá cho sản phẩm với id: " + productDetailId));
        return PriceResponse.formPrice(price);
    }

    @Override
    public void deletePrice(int id) throws DataNotFoundException {
        getPriceById(id);
        priceRepository.deleteById(id);

    }

    @Override
    public PriceResponse updatePrice(int priceId, PriceDTO priceDTO) throws DataNotFoundException {
        Price existingPrice = getPriceById(priceId);

        // Kiểm tra ngày bắt đầu không nhỏ hơn hôm nay
        if (priceDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Ngày bắt đầu không thể nhỏ hơn hôm nay.");
        }

        // Kiểm tra ngày kết thúc phải lớn hơn và khác ngày bắt đầu
        if (!priceDTO.getEndDate().isAfter(priceDTO.getStartDate())) {
            throw new DataNotFoundException("Ngày kết thúc phải lớn hơn và khác ngày bắt đầu.");
        }

        // Tính toán giá khuyến mãi nếu có tỷ lệ phần trăm
        Float promotionPrice = priceDTO.getPromotionPrice();
        if (promotionPrice != null && promotionPrice < 0) {
            throw new IllegalArgumentException("Không thể nhập % là âm.");
        }
        Float calculatedPromotionPrice = null;

        if (promotionPrice != null && promotionPrice > 0) {
            // Tính giá khuyến mãi từ tỷ lệ phần trăm
            calculatedPromotionPrice = priceDTO.getSellingPrice() * (1 - promotionPrice / 100);
        }
        existingPrice.setSellingPrice(priceDTO.getSellingPrice());
        existingPrice.setPromotionPrice(calculatedPromotionPrice);
        existingPrice.setStartDate(priceDTO.getStartDate());
        existingPrice.setEndDate(priceDTO.getEndDate());

        existingPrice = priceRepository.save(existingPrice);
        return PriceResponse.formPrice(existingPrice);
    }


//    public List<Price> createPrice(PriceDTO priceDTO) throws DataNotFoundException {
//        // Lấy danh sách ID của ProductDetail
//        List<Integer> productDetailIds = priceDTO.getProductDetailId();
//
//        // Kiểm tra danh sách không rỗng
//        if (productDetailIds == null || productDetailIds.isEmpty()) {
//            throw new IllegalArgumentException("Danh sách product_detail_id không được rỗng.");
//        }
//
//        // Kiểm tra ngày bắt đầu không nhỏ hơn hôm nay
//        if (priceDTO.getStartDate().isBefore(LocalDate.now())) {
//            throw new IllegalArgumentException("Ngày bắt đầu không thể nhỏ hơn hôm nay.");
//        }
//
//        // Kiểm tra ngày kết thúc nếu có
//        if (priceDTO.getEndDate() != null && !priceDTO.getEndDate().isAfter(priceDTO.getStartDate())) {
//            throw new IllegalArgumentException("Ngày kết thúc phải lớn hơn và khác ngày bắt đầu.");
//        }
//
//        // Tính giá khuyến mãi (nếu có)
//        Float calculatedPromotionPrice = null;
//        if (priceDTO.getPromotionPrice() != null && priceDTO.getPromotionPrice() >= 0) {
//            calculatedPromotionPrice = priceDTO.getSellingPrice() * (1 - priceDTO.getPromotionPrice() / 100);
//        }
//
//        List<Price> createdPrices = new ArrayList<>();
//
//        // Xử lý từng ProductDetail ID
//        for (Integer productDetailId : productDetailIds) {
//            ProductDetail existingProductDetail = productDetailRepository
//                    .findById(productDetailId)
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Không tìm thấy sản phẩm với id: " + productDetailId
//                    ));
//
//            // Cập nhật endDate cho giá hiện tại (nếu có)
//            Price existingPrice = priceRepository
//                    .findFirstByProductDetailIdAndEndDateIsNull(productDetailId);
//            if (existingPrice != null) {
//                existingPrice.setEndDate(LocalDate.now());
//                priceRepository.save(existingPrice);
//            }
//
//            // Tạo giá mới
//            Price newPrice = Price.builder()
//                    .sellingPrice(priceDTO.getSellingPrice())
//                    .promotionPrice(calculatedPromotionPrice)
//                    .startDate(priceDTO.getStartDate())
//                    .endDate(priceDTO.getEndDate())
//                    .productDetail(existingProductDetail)
//                    .build();
//
//            createdPrices.add(priceRepository.save(newPrice));
//        }
//
//        return createdPrices; // Trả về danh sách các giá vừa tạo
//    }

}
