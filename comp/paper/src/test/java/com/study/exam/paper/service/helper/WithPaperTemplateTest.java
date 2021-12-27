package com.study.exam.paper.service.helper;


import com.study.exam.paper.domain.Problem;
import com.study.exam.paper.repository.PaperTemplateRepository;
import com.study.exam.paper.repository.ProblemRepository;
import com.study.exam.paper.service.PaperTemplateService;
import com.study.exam.paper.service.ProblemService;
import com.study.exam.user.service.helper.WithUserTest;
import com.study.exam.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

public class WithPaperTemplateTest extends WithUserTest {

    @Autowired
    protected PaperTemplateRepository paperTemplateRepository;
    @Autowired
    protected ProblemRepository problemRepository;

    protected PaperTemplateService paperTemplateService;
    protected PaperTemplateTestHelper paperTemplateTestHelper;
    protected ProblemService problemService;
    protected User teacher;

    protected void preparePaperTemplate(){
        this.problemRepository.deleteAll();
        this.paperTemplateRepository.deleteAll();
        prepareUserServices();

        this.problemService = new ProblemService(problemRepository);
        this.paperTemplateService = new PaperTemplateService(paperTemplateRepository, problemService);
        this.paperTemplateTestHelper = new PaperTemplateTestHelper(this.paperTemplateService);

        this.teacher = this.userTestHelper.createTeacher(school, "선생님1");
    }

    protected Problem problem(long ptId, String content, String answer){
        return Problem.builder().paperTemplateId(ptId)
                .content(content).answer(answer).build();
    }

}
