package com.study.exam.paper.service.helper;

import com.study.exam.paper.domain.PaperTemplate;
import com.study.exam.paper.domain.Problem;
import com.study.exam.paper.service.PaperTemplateService;
import com.study.exam.user.domain.User;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class PaperTemplateTestHelper {

    private final PaperTemplateService paperTemplateService;

    public PaperTemplate createPaperTemplate(User teacher, String paperName){
        PaperTemplate paperTemplate = PaperTemplate.builder()
                .name(paperName)
                .creator(teacher)
                .build();
        return paperTemplateService.save(paperTemplate);
    }

    public Problem addProblem(long paperTemplateId, Problem problem){
        return paperTemplateService.addProblem(paperTemplateId, problem);
    }

    public static void assertPaperTemplate(PaperTemplate pt, User user, String paperName) {
        assertNotNull(pt.getPaperTemplateId());
        assertNotNull(pt.getCreated());
        assertNotNull(pt.getUpdated());
        assertEquals(paperName, pt.getName());
        assertEquals(user.getUserId(), pt.getCreator().getUserId());
    }

}