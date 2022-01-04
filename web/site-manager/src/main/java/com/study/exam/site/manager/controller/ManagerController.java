package com.study.exam.site.manager.controller;

import com.study.exam.user.service.SchoolService;
import com.study.exam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {


    private final SchoolService schoolService;
    private final UserService userService;

    @GetMapping({"", "/"})
    public String index(Model model){
        model.addAttribute("schoolCount", schoolService.count());
        model.addAttribute("teacherCount", userService.countTeacher());
        model.addAttribute("studentCount", userService.countStudent());
        return "manager/index";
    }
}
