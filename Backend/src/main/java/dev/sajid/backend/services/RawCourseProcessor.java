package dev.sajid.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.models.normalized.course.Program;
import dev.sajid.backend.models.normalized.course.Scheme;
import dev.sajid.backend.models.normalized.course.Subject;
import dev.sajid.backend.models.raw.Course;

@Component
public interface RawCourseProcessor {
    void processRawCourses(List<Course> rawCourses);
    Map<String, Scheme> findOrCreateSchemes(List<Course> rawCourses);
    Map<Integer, Branch> findOrCreateBranches(List<Course> rawCourses);
    Map<ProgramKey, Program> findOrCreatePrograms(List<Course> rawCourses, Map<String, Scheme> schemesMap, Map<Integer, Branch> branchesMap);// also need scheme, branch repos
    Map<String, Subject> findOrCreateSubjects(List<Course> rawCourses);
    void createProgramSubjects(List<Course> rawCourses, Map<String, Scheme> schemesMap, Map<Integer, Branch> branchesMap, Map<ProgramKey, Program> programsMap, Map<String, Subject> subjectsMap); 
}
