package dev.sajid.backend.repositories;

import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Integer>{
    @Query("select f.courseAssignments from Faculty f where f.code = :facultyId")
    List<CourseAssignment> findCourseAssignmentsById(@Param("facultyId") String facultyId);

    @Query("select f from Faculty f where f.code = :username")
    Optional<Faculty> findByUsername(@Param("username") String username);

    boolean existsByCode(String facultyCode);
}
