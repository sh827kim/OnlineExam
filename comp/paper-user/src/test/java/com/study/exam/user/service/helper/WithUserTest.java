package com.study.exam.user.service.helper;

import com.study.exam.user.domain.School;
import com.study.exam.user.repository.SchoolRepository;
import com.study.exam.user.repository.UserRepository;
import com.study.exam.user.service.SchoolService;
import com.study.exam.user.service.UserSecurityService;
import com.study.exam.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

public class WithUserTest {

    @Autowired
    protected SchoolRepository schoolRepository;
    @Autowired
    protected UserRepository userRepository;

    protected SchoolService schoolService;
    protected UserService userService;
    protected UserSecurityService userSecurityService;

    protected SchoolTestHelper schoolTestHelper;
    protected UserTestHelper userTestHelper;
    protected School school;

    private boolean prepared;

    protected void prepareUserServices (){
        if(prepared) return;
        prepared = true;

        this.schoolRepository.deleteAll();
        this.userRepository.deleteAll();
        this.schoolService = new SchoolService(schoolRepository);
        this.userService = new UserService(schoolRepository, userRepository);
        this.userSecurityService = new UserSecurityService(userRepository);

        this.userTestHelper = new UserTestHelper(userService, NoOpPasswordEncoder.getInstance());
        this.schoolTestHelper = new SchoolTestHelper(schoolService);
        this.school = this.schoolTestHelper.createSchool("테스트 학교", "서울");
    }

}
