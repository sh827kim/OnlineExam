package com.study.exam.web.config;

import com.study.exam.user.domain.Authority;
import com.study.exam.user.domain.School;
import com.study.exam.user.domain.User;
import com.study.exam.user.service.SchoolService;
import com.study.exam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final SchoolService schoolService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var defaultSchool = schoolService.findSchool(1L).orElseGet(()->
            schoolService.save(School.builder()
                    .name("테스트 학교")
                    .city("서울")
                    .build())
        );
        userService.findByEmail("admin@test.com").or(()->{
            var admin = User.builder()
                    .name("admin")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .enabled(true)
                    .school(defaultSchool)
                    .build();
            admin = userService.save(admin);
            admin.setAuthorities(Set.of(
                    new Authority(admin.getUserId(), Authority.ROLE_ADMIN),
                    new Authority(admin.getUserId(), Authority.ROLE_TEACHER),
                    new Authority(admin.getUserId(), Authority.ROLE_STUDENT)));
            return Optional.of(userService.save(admin));
        });
        userService.findByEmail("teacher@test.com").or(()->{
            var teacher = User.builder()
                    .name("teacher")
                    .email("teacher@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .enabled(true)
                    .school(defaultSchool)
                    .build();
            teacher = userService.save(teacher);
            teacher.setAuthorities(Set.of(
                    new Authority(teacher.getUserId(), Authority.ROLE_TEACHER)
            ));
            return Optional.of(userService.save(teacher));
        });
        userService.findByEmail("study@test.com").or(()->{
            var study = User.builder()
                    .name("study")
                    .email("study@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .enabled(true)
                    .school(defaultSchool)
                    .build();
            study = userService.save(study);
            study.setAuthorities(Set.of(
                    new Authority(study.getUserId(), Authority.ROLE_STUDENT)
            ));
            return Optional.of(userService.save(study));
        });

    }
}
