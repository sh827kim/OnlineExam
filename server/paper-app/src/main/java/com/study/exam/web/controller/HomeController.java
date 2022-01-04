package com.study.exam.web.controller;

import com.study.exam.user.domain.Authority;
import com.study.exam.user.domain.School;
import com.study.exam.user.domain.User;
import com.study.exam.user.service.SchoolService;
import com.study.exam.user.service.UserService;
import com.study.exam.web.controller.vo.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SchoolService schoolService;
    private final UserService userService;

    private RequestCache requestCache =  new HttpSessionRequestCache();


    @GetMapping("/")
    public String home(Model model){
        return "index";
    }

    @ResponseBody
    @GetMapping("/schools")
    public List<School> getSchoolList(@RequestParam(value = "city", required = true) String city) {
        return schoolService.findAllByCity(city);
    }

    @ResponseBody
    @GetMapping("/teachers")
    public List<UserData> getTeacherList(@RequestParam(value = "schoolId", required = true) Long schoolId) {
        return userService.findBySchoolTeacherList(schoolId).stream()
                .map(user -> new UserData(user.getUserId(), user.getName())).toList();
    }

    @GetMapping("/login")
    public String login(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "site", required = false) String site,
            @RequestParam(value = "error", defaultValue = "false") Boolean error,
            HttpServletRequest request,
            Model model){

        if(user!=null && user.isEnabled()){
            if(user.getAuthorities().contains(Authority.ADMIN_AUTHORITY)){
                return "redirect:/manager";
            }else if(user.getAuthorities().contains(Authority.TEACHER_AUTHORITY)){
                return "redirect:/teacher";
            }else if(user.getAuthorities().contains(Authority.STUDENT_AUTHORITY)){
                return "redirect:/study";
            }
        }

        if(site==null) {
            var savedRequest = requestCache.getRequest(request, null);
            site = savedRequest!=null ? estimateSite(savedRequest.getRedirectUrl()) : null;
        }

        model.addAttribute("error", error);
        model.addAttribute("site", site);
        return "loginForm";
    }


    @GetMapping("/signup")
    public String signUp(
            @RequestParam String site,
            HttpServletRequest request){

        site = site==null ? estimateSite(request.getParameter("referer")) : site;

        return "redirect:/"+"signup/" +site;
    }

    private String estimateSite(String referer) {
        if(referer!=null) {
            try {
                var url = new URL(referer);
                var path = url.getPath();
                if(path != null){
                    if(path.startsWith("/teacher") || path.startsWith("/site=teacher")) return "teacher";
                    if(path.startsWith("/manager") || path.startsWith("/site=manager")) return "manager";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return "student";
    }
}