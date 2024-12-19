package com.example.shopapp_api.dtos.responses.review;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.entities.review.Reply;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class ReplyResponse extends BaseResponse {


    private String content;

    @JsonProperty("review")
    private int review;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("rating")
    private Float rating;

    private int user;

    public static ReplyResponse formReply(Reply reply) {

        ReplyResponse replyResponse = ReplyResponse.builder()

                .content(reply.getContent())
                .review(reply.getReview().getId())
                .comment(reply.getReview().getComment())
//                .rating(reply.getReview().getRating())
                .user(reply.getUser().getId())

                .build();
        replyResponse.setCreatedAt(reply.getCreatedAt());
        replyResponse.setUpdatedAt(reply.getUpdatedAt());

        return replyResponse;
    }
}
