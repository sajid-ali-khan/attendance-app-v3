package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.faculty.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Integer>{
    
}
