package com.jnu.capstone.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter  // 추가!
@Entity
@Table(name = "post_keyword")
@IdClass(PostKeywordId.class)
public class PostKeyword {

    @Id
    @Column(name = "keyword_text", nullable = false, length = 50)
    private String keywordText;

    @Id
    @Column(name = "post_id", nullable = false)
    private int postId;

}
