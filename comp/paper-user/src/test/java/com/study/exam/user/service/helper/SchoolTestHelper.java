package com.study.exam.user.service.helper;

import com.study.exam.user.domain.School;
import com.study.exam.user.service.SchoolService;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class SchoolTestHelper {
    private final SchoolService schoolService;

    public static School makeSchool(String name, String city) {
        return School.builder()
                .name(name)
                .city(city)
                .build();
    }

    public School createSchool(String name, String city) {
        return schoolService.save(makeSchool(name, city));
    }

    public static void assertSchool(School school, String name, String city) {
        assertNotNull(school.getSchoolId());
        assertNotNull(school.getCreated());
        assertNotNull(school.getUpdated());

        assertEquals(name, school.getName());
        assertEquals(city, school.getCity());
    }
}
