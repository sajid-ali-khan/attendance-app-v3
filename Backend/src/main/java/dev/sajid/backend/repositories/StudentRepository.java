package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.student.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{
    
}
