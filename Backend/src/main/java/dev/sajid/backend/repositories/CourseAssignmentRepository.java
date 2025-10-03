package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.derived.CourseAssignment;

public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Integer> {
    
}
