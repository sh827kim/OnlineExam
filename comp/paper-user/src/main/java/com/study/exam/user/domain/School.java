package com.study.exam.user.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    private String name;

    private String city;

    @Column(updatable = false)
    private LocalDateTime created;

    private LocalDateTime updated;

    @Transient
    private Long teacherCount;

    @Transient
    private Long studentCount;
}