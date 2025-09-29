package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.student.StudentBatch;

public interface StudentBatchRepository extends JpaRepository<StudentBatch, Integer>{
    
}
