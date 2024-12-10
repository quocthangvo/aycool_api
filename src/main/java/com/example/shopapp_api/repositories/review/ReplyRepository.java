package com.example.shopapp_api.repositories.review;

import com.example.shopapp_api.entities.review.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByReviewId(int reviewId);
}
