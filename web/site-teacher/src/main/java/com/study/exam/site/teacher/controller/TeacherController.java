package com.study.exam.site.teacher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/teacher")
public class TeacherController {


    @GetMapping({"", "/"})
    public String index(){

        return "/templates/teacher/index";
    }

    @GetMapping("/signup")
    public String signUp(){

        return "/templates/teacher/signup";
    }

}
