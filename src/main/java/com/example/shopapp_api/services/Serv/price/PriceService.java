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
import java.util.List;

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

        // Kiểm tra ngày kết thúc phải lớn hơn và khác ngày bắt đầu
        if (!priceDTO.getEndDate().isAfter(priceDTO.getStartDate())) {
            throw new DataNotFoundException("Ngày kết thúc phải lớn hơn và khác ngày bắt đầu.");
        }

        // Tính giá khuyến mãi dựa trên tỷ lệ phần trăm nếu có
        Float promotionPrice = priceDTO.getPromotionPrice();
        if (promotionPrice != null && promotionPrice < 0) {
            throw new IllegalArgumentException("Không thể nhập % là âm.");
        }
        Float calculatedPromotionPrice = null;
        if (promotionPrice != null && promotionPrice > 0) {
            // Tính giá khuyến mãi bằng cách áp dụng phần trăm khuyến mãi
            calculatedPromotionPrice = priceDTO.getSellingPrice() * (1 - promotionPrice / 100);
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
    public List<Price> getAllPriceByProductDetailId(int productDetailId) {
        return priceRepository.findByProductDetailId(productDetailId);
    }

    @Override
    public Price getPriceByProductDetailId(int productDetailId) {
        return priceRepository.findTopByProductDetailIdOrderByCreatedAtDesc(productDetailId);
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
}
