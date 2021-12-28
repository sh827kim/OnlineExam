package com.study.exam.site.manager.controller;

import com.study.exam.site.manager.controller.vo.StudentData;
import com.study.exam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/student")
@RequiredArgsConstructor
public class StudentMngController {

    private final UserService userService;

    @GetMapping("/list")
    public String list(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            Model model
    ){
        model.addAttribute("menu", "student");
        Page<StudentData> studentList = userService.listStudents(pageNum, size)
                .map(s -> new StudentData(s.getSchool().getName(), s.getUserId(), s.getName(), s.getEmail(), s.getGrade()));
        model.addAttribute("page", studentList);

        return "manager/student/list";
    }


}
