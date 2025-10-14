package dev.sajid.backend.services.csv;

import java.util.List;
import java.util.Map;

import dev.sajid.backend.services.keys.BranchKey;
import dev.sajid.backend.services.keys.SubjectKey;
import org.springframework.stereotype.Component;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.models.normalized.course.Scheme;
import dev.sajid.backend.models.normalized.course.Subject;
import dev.sajid.backend.models.raw.Course;

@Component
public interface RawCourseProcessor {
    void processRawCourses(List<Course> rawCourses);

    Map<String, Scheme> findOrCreateSchemes(List<Course> rawCourses);

    Map<BranchKey, Branch> findOrCreateBranches(List<Course> rawCourses, Map<String, Scheme> schemesMap);

    Map<SubjectKey, Subject> findOrCreateSubjects(List<Course> rawCourses);

    void createBranchSubjects(
            List<Course> rawCourses, Map<String, Scheme> schemesMap, Map<BranchKey, Branch> branchesMap,
            Map<SubjectKey, Subject> subjectsMap);
}
