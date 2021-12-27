package com.study.exam.paper.repository;

import com.study.exam.paper.domain.PaperAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperAnswerRepository extends JpaRepository<PaperAnswer, PaperAnswer.PaperAnswerId> {

}