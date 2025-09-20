package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.sajid.backend.models.normalized.course.Subject;
import java.util.Optional;


public interface SubjectRepository extends JpaRepository<Subject, Integer>{
    Optional<Subject> findByCode(String code);
    boolean existsByCode(String code);
}
