package com.study.exam.site.teacher.controller;

import com.study.exam.paper.domain.Paper;
import com.study.exam.paper.domain.PaperTemplate;
import com.study.exam.paper.domain.Problem;
import com.study.exam.paper.service.PaperService;
import com.study.exam.paper.service.PaperTemplateService;
import com.study.exam.site.teacher.controller.vo.ProblemInput;
import com.study.exam.user.domain.User;
import com.study.exam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value="/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;
    private final PaperTemplateService paperTemplateService;
    private final PaperService paperService;

    @GetMapping({"", "/"})
    public String index(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("studentCount", userService.findTeacherStudentCount(user.getUserId()));
        model.addAttribute("paperTemplateCount", paperTemplateService.countByUserId(user.getUserId()));
        return "teacher/index";
    }

    @GetMapping("/student/list")
    public String studyList(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "student");
        model.addAttribute("studentList", userService.findTeacherStudentList(user.getUserId()));
        return "teacher/student/list";
    }

    /**
     * 시험을 본 학생과 보지 않은 학생 리스트와 결과... 리스트
     *
     * @param paperTemplateId
     * @param user
     * @param model
     * @return
     */
    @GetMapping("/student/results")
    public String studyResults(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("menu", "paper");

        var papers = paperService.getPapers(paperTemplateId);
        var userMap = userService.getUsers(papers.stream().map(Paper::getStudyUserId).toList());
        papers.forEach(paper -> paper.setUser(userMap.get(paper.getStudyUserId())));

        model.addAttribute("template", paperTemplateService.findById(paperTemplateId).orElseThrow(IllegalArgumentException::new));
        model.addAttribute("papers", papers);
        return "teacher/student/results";
    }

    @GetMapping("/paperTemplate/list")
    public String paperTemplateList(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("menu", "paper");
        var templateList = paperTemplateService.findByTeacherId(user.getUserId(), pageNum, size);

        model.addAttribute("page", templateList);
        return "teacher/paperTemplate/list";
    }

    @GetMapping("/paperTemplate/create")
    public String editPaperTemplateName(@AuthenticationPrincipal User user, Model model){

        return "teacher/paperTemplate/create";
    }

    @PostMapping(value="/paperTemplate/create", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String createAndEditTemplate(@RequestParam String paperName, @AuthenticationPrincipal User user, Model model){

        var paperTemplate = PaperTemplate.builder()
                .name(paperName)
                .userId(user.getUserId())
                .build();

        model.addAttribute("template", paperTemplateService.save(paperTemplate));
        return "teacher/paperTemplate/edit";
    }

    @GetMapping("/paperTemplate/edit")
    public String editPaperTemplate(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user,
            Model model
    ){
        var paperTemplate = paperTemplateService.findPaperTemplate(paperTemplateId)
                .orElseThrow(() -> new IllegalArgumentException("시험지 템플릿이 존재하지 않습니다."));

        model.addAttribute("template", paperTemplate);
        return "teacher/paperTemplate/edit";
    }

    @PostMapping(value="/paperTemplate/problem/add", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addProblemToPaperTemplate(
            ProblemInput problemInput,
            @AuthenticationPrincipal User user, Model model){

        var p = Problem.builder()
                .paperTemplateId(problemInput.getPaperTemplateId())
                .content(problemInput.getContent())
                .answer(problemInput.getAnswer())
                .build();
        paperTemplateService.addProblem(problemInput.getPaperTemplateId(), p);

        var paperTemplate = paperTemplateService.findPaperTemplate(problemInput.getPaperTemplateId())
                .orElseThrow(() -> new IllegalArgumentException("시험지 템플릿이 존재하지 않습니다."));

        model.addAttribute("template", paperTemplate);
        return "teacher/paperTemplate/edit";
    }

    /**
     * 시험지 배포
     * @return
     */
    @GetMapping("/paperTemplate/publish")
    public String publishPaper(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user, Model model
    ){

        var studentList = userService.findTeacherStudentList(user.getUserId());
        paperService.publishPaper(paperTemplateId, studentList.stream().map(User::getUserId).toList());
        return "redirect:teacher/paperTemplate/list";
    }
}
