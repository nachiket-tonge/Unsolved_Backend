package com.Project.Unsolved.dto;



import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private String content;

    private String userName;   // name from BaseProfile

    private LocalDateTime createdAt;
}
