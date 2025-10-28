package dev.sajid.backend.repositories;

import java.util.List;
import java.util.Optional;

import dev.sajid.backend.models.normalized.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.sajid.backend.models.normalized.derived.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsByStudentBatchIdAndBranchSubjectId(int studentBatchId, int branchSubjectId);
    Optional<Course> findByStudentBatchIdAndBranchSubjectId(int studentBatchId, int branchSubjectId);

    @Query("select c.studentBatch.students from Course c where c.id = :courseId")
    List<Student> findStudentListById(@Param("courseId") int courseId);
}
