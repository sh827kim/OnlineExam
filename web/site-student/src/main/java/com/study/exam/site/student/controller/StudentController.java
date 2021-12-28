package com.study.exam.site.student.controller;

import com.study.exam.paper.domain.Paper;
import com.study.exam.paper.domain.PaperTemplate;
import com.study.exam.paper.domain.Problem;
import com.study.exam.site.student.controller.vo.Answer;
import com.study.exam.user.domain.School;
import com.study.exam.user.domain.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value="/student")
public class StudentController {
    @GetMapping({"", "/"})
    public String index(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("paperCount", 1);
        model.addAttribute("resultCount", 1);

        return "student/index";
    }

    @GetMapping("/signup")
    public String signUp(){

        return "student/signup";
    }
    // 시험지 리스트
    @GetMapping("/papers")
    public String paperList(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "paper");
        model.addAttribute("papers", paperList());
        return "student/paper/papers.html";
    }

    @GetMapping("/results")
    public String results(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal User user, Model model
    ){
        model.addAttribute("menu", "result");
        model.addAttribute("page",
                new PageImpl(paperList())
        );
        return "student/paper/results.html";
    }


    // 시험 보기
    @GetMapping(value="/paper/apply")
    public String applyPaper(@RequestParam Long paperId, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "paper");

        model.addAttribute("paperId", paperId);
        model.addAttribute("paperName", "테스트 시험지");

        model.addAttribute("problem", Problem.builder()
                .content("문제")
                .answer("정답")
                .indexNum(1)
                .paperTemplateId(1L)
                .build());
        model.addAttribute("alldone", false);

        return "student/paper/apply.html";
    }

    /**
     * TODO : 다른 사람이 풀수도 있음. 아이디를 확인해야 함.
     *
     * @return
     */
    // 정답 제출
    @PostMapping(value="/paper/answer", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String answer(Answer answer, @AuthenticationPrincipal User user, Model model){

        return "redirect:student/paper/apply.html?paperId="+answer.getPaperId();
    }

    // 시험 완료
    @GetMapping("/paper/done")
    public String donePaper(Long paperId){

        return "redirect:student/paper/result.html?paperId="+paperId;
    }

    // 결과 시험지 리스트
    @GetMapping("/paper/result")
    public String paperResult(Long paperId, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "result");
        model.addAttribute("paper", paperList().get(0));
        return "student/paper/result.html";
    }

    private User user(){
        return User.builder()
                .userId(1L)
                .name("홍길동")
                .email("hong@test.com")
                .grade("3")
                .enabled(true)
                .school(School.builder().schoolId(1L).name("테스트 학교").city("서울").build())
                .build();
    }

    private PaperTemplate paperTemplate(){
        return PaperTemplate.builder()
                .paperTemplateId(1L)
                .name("테스트 시험지")
                .creator(user())
                .userId(1L)
                .publishedCount(1)
                .build();
    }

    private List<Paper> paperList(){
        return List.of(Paper.builder()
                .name("테스트 시험지")
                .paperTemplateId(1L)
                .state(Paper.PaperState.START)
                .total(2)
                .paperId(1L)
                .studyUserId(1L)
                .user(user())
                .build());
    }
}
