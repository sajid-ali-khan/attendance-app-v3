package dev.sajid.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.course.Branch;


public interface BranchRepository extends JpaRepository<Branch, Integer>{
    Optional<Branch> findByShortForm(String shortForm);

    boolean existsByShortForm(String shortForm);

    @Query("select b.id from Branch b where b.shortForm = :shortForm")
    int findIdByShortForm(@Param("shortForm") String shortForm);

    Optional<Branch> findByBranchCode(int branchCode);
}
