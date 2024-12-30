package com.example.shopapp_api.services.Impl.review;

import com.example.shopapp_api.dtos.requests.review.ReplyDTO;

import com.example.shopapp_api.entities.review.Reply;


public interface IReplyService {
    Reply createReply(ReplyDTO replyDTO);


}
