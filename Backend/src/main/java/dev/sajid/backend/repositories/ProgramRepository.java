package dev.sajid.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.course.Program;

public interface ProgramRepository extends JpaRepository<Program, Integer>{
    @Query("""
            select case when count(p) > 0 then true else false end
            from Program p
            where p.scheme.code = :schemeCode
            and p.branch.branchCode = :branchCode
            """)
    boolean existsBySchemeAndBranch(@Param("schemeCode") String schemeCode,@Param("branchCode") int branchCode);

    @Query("""
            select p from Program p
            where p.scheme.code = :schemeCode
            and p.branch.branchCode = :branchCode
            """)
    Optional<Program> findBySchemeAndBranch(@Param("schemeCode") String schemeCode,@Param("branchCode") int branchCode);
}
