package com.study.exam.site.student.controller;

import com.study.exam.paper.domain.Paper;
import com.study.exam.paper.service.PaperService;
import com.study.exam.paper.service.PaperTemplateService;
import com.study.exam.site.student.controller.vo.Answer;
import com.study.exam.user.domain.User;
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
@RequestMapping(value="/student")
@RequiredArgsConstructor
public class StudentController {

    private final PaperTemplateService paperTemplateService;

    private final PaperService paperService;

    @GetMapping({"", "/"})
    public String index(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("paperCount", paperService.countPapersByUserId(user.getUserId()));
        model.addAttribute("resultCount", paperService.countPapersByUserResult(user.getUserId()));

        return "student/index";
    }

    // 시험지 리스트
    @GetMapping("/papers")
    public String paperList(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "paper");
        model.addAttribute("papers", paperService.getPapersByUserId(user.getUserId()));
        return "student/paper/papers";
    }

    @GetMapping("/results")
    public String results(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal User user, Model model
    ){
        model.addAttribute("menu", "result");
        model.addAttribute("page",
                paperService.getPapersByUserResult(user.getUserId(), pageNum, size)
        );
        return "student/paper/results";
    }


    // 시험 보기
    @GetMapping(value="/paper/apply")
    public String applyPaper(@RequestParam Long paperId, @AuthenticationPrincipal User user, Model model){

        var paper = paperService.findPaper(paperId).orElseThrow(IllegalArgumentException::new);
        if(paper.getState() == Paper.PaperState.END){
            return "redirect:/student/paper/result?paperId="+paperId;
        }

        var answerMap = paper.answerMap();

        var template = paperTemplateService.findById(paper.getPaperTemplateId())
                .orElseThrow(() -> new IllegalArgumentException(paper.getPaperTemplateId() + " 시험지가 존재하지 않습니다."));

        var notAnswered = template.getProblemList().stream().filter(problem -> !answerMap.containsKey(problem.getIndexNum())).findFirst();


        model.addAttribute("menu", "paper");

        model.addAttribute("paperId", paperId);
        model.addAttribute("paperName", paper.getName());

        notAnswered.ifPresent(problem -> model.addAttribute("problem", problem));

        model.addAttribute("alldone", notAnswered.isEmpty());

        return "student/paper/apply";
    }

    /**
     * TODO : 다른 사람이 풀수도 있음. 아이디를 확인해야 함.
     *
     * @return
     */
    // 정답 제출
    @PostMapping(value="/paper/answer", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String answer(Answer answer, @AuthenticationPrincipal User user, Model model){
        paperService.answer(answer.getPaperId(), answer.getProblemId(), answer.getIndexNum(), answer.getAnswer());
        return "redirect:/student/paper/apply?paperId="+answer.getPaperId();
    }

    // 시험 완료
    @GetMapping("/paper/done")
    public String donePaper(Long paperId){
        paperService.paperDone(paperId);
        return "redirect:/student/paper/result?paperId="+paperId;
    }

    // 결과 시험지 리스트
    @GetMapping("/paper/result")
    public String paperResult(Long paperId, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("menu", "result");
        var paper = paperService.findPaper(paperId).orElseThrow(()-> new IllegalArgumentException("시험지가 존재하지 않습니다."));
        model.addAttribute("paper", paper);

        return "student/paper/result";
    }
}
