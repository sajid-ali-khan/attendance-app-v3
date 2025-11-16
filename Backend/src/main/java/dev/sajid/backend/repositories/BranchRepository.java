package dev.sajid.backend.repositories;

import java.util.List;
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

    @Query("select b.shortForm from Branch b where b.branchCode = :branchCode")
    List<String> findShortFormByBranchCode(@Param("branchCode") int branchCode);

    @Query("select b from Branch b where b.scheme.code = :schemeCode")
    List<Branch> findByScheme(@Param("schemeCode") String schemeCode);

    @Query("select b from Branch b where b.scheme.code = :schemeCode and b.branchCode = :branchCode")
    Optional<Branch> findBySchemeAndBranch(@Param("schemeCode") String schemeCode, @Param("branchCode") int branchCode);

}
