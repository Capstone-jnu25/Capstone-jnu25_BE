package com.jnu.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AiResponseDto {
    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("postIds")
    private List<Integer> postIds;
}
