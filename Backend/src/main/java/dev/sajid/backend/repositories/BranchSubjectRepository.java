package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sajid.backend.models.normalized.course.BranchSubject;

public interface BranchSubjectRepository extends JpaRepository<BranchSubject, Integer>{

}
