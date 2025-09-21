package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.course.ProgramSubject;

public interface ProgramSubjectRepository extends JpaRepository<ProgramSubject, Integer>{
    
}
