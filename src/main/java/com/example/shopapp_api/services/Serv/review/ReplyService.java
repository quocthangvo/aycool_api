package com.example.shopapp_api.services.Serv.review;

import com.example.shopapp_api.dtos.requests.review.ReplyDTO;
import com.example.shopapp_api.dtos.responses.review.ReplyResponse;

import com.example.shopapp_api.entities.review.Reply;
import com.example.shopapp_api.repositories.review.ReplyRepository;
import com.example.shopapp_api.services.Impl.review.IReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


@Service
@RequiredArgsConstructor
@RestController
public class ReplyService implements IReplyService {
    private final ReplyRepository replyRepository;

    @Override
    public Reply createReply(ReplyDTO replyDTO) {
        // Tạo đối tượng Reply từ ReplyDTO
        Reply reply = Reply.builder()
                .content(replyDTO.getContent())  // Lấy nội dung từ ReplyDTO
                .build();

        // Lưu đối tượng Reply vào cơ sở dữ liệu
        Reply savedReply = replyRepository.save(reply);

        // Trả về đối tượng Reply đã lưu
        return savedReply;
    }
}
