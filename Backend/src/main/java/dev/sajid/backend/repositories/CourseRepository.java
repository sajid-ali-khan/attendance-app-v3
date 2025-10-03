package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.derived.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsByStudentBatchIdAndBranchSubjectId(int studentBatchId, int branchSubjectId);
}
