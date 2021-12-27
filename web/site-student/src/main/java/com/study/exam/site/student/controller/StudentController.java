package com.study.exam.site.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/student")
public class StudentController {
    @GetMapping({"", "/"})
    public String index(){

        return "/student/index";
    }

    @GetMapping("/signup")
    public String signUp(){

        return "/student/signup";
    }
}
