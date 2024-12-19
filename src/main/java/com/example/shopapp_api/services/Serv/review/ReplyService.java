//package com.example.shopapp_api.services.Serv.review;
//
//import com.example.shopapp_api.dtos.requests.review.ReplyDTO;
//import com.example.shopapp_api.dtos.responses.review.ReplyResponse;
//import com.example.shopapp_api.entities.review.Reply;
//import com.example.shopapp_api.entities.review.Review;
//import com.example.shopapp_api.entities.users.Role;
//import com.example.shopapp_api.entities.users.User;
//import com.example.shopapp_api.repositories.review.ReplyRepository;
//import com.example.shopapp_api.repositories.review.ReviewRepository;
//import com.example.shopapp_api.repositories.user.UserRepository;
//import com.example.shopapp_api.services.Impl.review.IReplyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@RestController
//public class ReplyService implements IReplyService {
////    private final ReviewRepository reviewRepository;
////    private final ReplyRepository replyRepository;
////    private final UserRepository userRepository;
////
////    @Override
////    public ReplyResponse createReply(ReplyDTO replyDTO) {
////        // Kiểm tra đánh giá có tồn tại không
////        Review review = reviewRepository.findById(replyDTO.getReviewId())
////                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại"));
////
////        // Kiểm tra người dùng có tồn tại không
////        User user = userRepository.findById(replyDTO.getUserId())
////                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
////
////
////        // Kiểm tra quyền của người dùng (phải là admin)
////        if (!user.getRole().getName().equals("ADMIN")) {  // Kiểm tra nếu role của người dùng là admin
////            throw new RuntimeException("Bạn không có quyền trả lời đánh giá này");
////        }
////
////        // Tạo mới phản hồi
////        Reply reply = new Reply();
////        reply.setReview(review);
////        reply.setUser(user);
////        reply.setContent(replyDTO.getContent());
////
////        replyRepository.save(reply);
////
////        // Trả về ReplyResponse
////        return ReplyResponse.formReply(reply);
////    }
//
////    @Override
////    public List<ReplyResponse> getRepliesByReviewId(int reviewId) {
////        // Lấy danh sách phản hồi theo reviewId
////        List<Reply> replies = replyRepository.findByReviewId(reviewId);
////
////        // Chuyển đổi danh sách Reply sang danh sách ReplyResponse
////        return replies.stream()
////                .map(ReplyResponse::formReply)
////                .collect(Collectors.toList());
////    }
//
//
//}
