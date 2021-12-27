package com.study.exam.paper.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sp_problem")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    private Long paperTemplateId;

    private int indexNum; // 1-based

    private String content;

    private String answer;

    @Column(updatable = false)
    private LocalDateTime created;

    private LocalDateTime updated;

}