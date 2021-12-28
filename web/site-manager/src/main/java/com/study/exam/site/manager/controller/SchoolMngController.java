package com.study.exam.site.manager.controller;


import com.study.exam.user.domain.School;
import com.study.exam.user.service.SchoolService;
import com.study.exam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/manager/school")
@RequiredArgsConstructor
public class SchoolMngController {

    private final SchoolService schoolService;

    private final UserService userService;

    @GetMapping("/list")
    public String list(
            @RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value="size", defaultValue = "10") Integer size,
            Model model){
        model.addAttribute("menu", "school");

        var schoolList = schoolService.getSchoolListAsPage(pageNum, size);
        schoolList.getContent().stream().forEach(school -> {
            school.setTeacherCount(userService.countTeacher(school.getSchoolId()));
            school.setStudentCount(userService.countStudent(school.getSchoolId()));
        });

        model.addAttribute("page", schoolList);

        return "manager/school/list.html";
    }

    @GetMapping("/edit")
    public String list(
            @RequestParam(value="schoolId", required = false) Long schoolId,
            Model model){
        model.addAttribute("menu", "school");
        var school = schoolId !=null ? schoolService.findSchool(schoolId) : School.builder().build();

        model.addAttribute("school",school);
        return "manager/school/edit.html";
    }

    @PostMapping(value = "/save", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String save(School school){
        schoolService.save(school);
        return "redirect:/manager/school/list.html";
    }

}
