package dev.sajid.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import dev.sajid.backend.models.normalized.course.Program;

public interface ProgramRepository extends JpaRepository<Program, Integer>{
    @Query("""
            select case when count(p) > 0 then true else false end
            from Program p
            where p.scheme.code = :schemeCode
            and p.branch.branchCode = :branchCode
            """)
    boolean existsBySchemeAndBranch(String schemeCode, int branchCode);
}
