package com.example.shopapp_api.controllers.review;

import com.example.shopapp_api.dtos.requests.review.ReplyDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.review.ReplyResponse;
import com.example.shopapp_api.entities.review.Reply;
import com.example.shopapp_api.services.Impl.review.IReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/replies")
@RequiredArgsConstructor
public class ReplyController {
    private final IReplyService replyService;

    @PostMapping("/reviews/reply")
    public ResponseEntity<ApiResponse<ReplyResponse>> replyToReview(
            @RequestBody ReplyDTO replyDTO) {
        ReplyResponse reply = replyService.createReply(replyDTO);
        return ResponseEntity.ok(new ApiResponse<>("Thành công", reply));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<List<ReplyResponse>>> getRepliesByReviewId(@PathVariable int reviewId) {
        // Lấy danh sách phản hồi của một đánh giá
        List<ReplyResponse> replyResponses = replyService.getRepliesByReviewId(reviewId);

        // Trả về kết quả
        return ResponseEntity.ok(new ApiResponse<>("Thành công", replyResponses));
    }

}
