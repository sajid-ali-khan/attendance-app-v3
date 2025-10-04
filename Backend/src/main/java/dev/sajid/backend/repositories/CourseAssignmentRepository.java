package dev.sajid.backend.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.derived.CourseAssignment;

public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Integer> {
        @Query("""
            select ca
            from CourseAssignment ca
            where ca.course.branchSubject.branch.id = :branchId
            and ca.course.studentBatch.semester = :semester
            and ca.course.studentBatch.section = :section
            """)
    List<CourseAssignment> findAssignments(@Param("branchId") Integer branchId, @Param("semester") Integer semester, @Param("section") String section);

}
