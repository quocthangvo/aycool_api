package com.example.shopapp_api.controllers.review;

import com.example.shopapp_api.dtos.requests.review.ReplyDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.review.ReplyResponse;
import com.example.shopapp_api.entities.review.Reply;
import com.example.shopapp_api.services.Impl.review.IReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/phan_hoi")
@RequiredArgsConstructor
public class ReplyController {
    private final IReplyService replyService;

    @PostMapping("")
    public ResponseEntity<Reply> createReply(@RequestBody ReplyDTO replyDTO) {
        // Gọi service để tạo phản hồi
        Reply createdReply = replyService.createReply(replyDTO);

        // Trả về phản hồi với mã trạng thái CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReply);
    }

}
