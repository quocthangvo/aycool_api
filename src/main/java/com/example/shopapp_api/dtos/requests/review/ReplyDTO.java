package com.example.shopapp_api.dtos.requests.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class ReplyDTO {

    private String content;

 
}
