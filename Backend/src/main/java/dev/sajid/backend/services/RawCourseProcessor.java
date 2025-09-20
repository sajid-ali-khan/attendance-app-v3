package dev.sajid.backend.services;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.sajid.backend.models.raw.Course;

@Component
public interface RawCourseProcessor {
    boolean processRawCourse(List<Course> rawCourses);
    boolean extractSchemes(List<Course> rawCourses);
    // boolean extractBranches(List<Course> rawCourses);
    // boolean extractPrograms(List<Course> rawCourses);// also need scheme, branch repos
    // boolean extractSubjects(List<Course> rawCourses);
    // boolean formProgramSubjects(); 
}
