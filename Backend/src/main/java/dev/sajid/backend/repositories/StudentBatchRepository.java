package dev.sajid.backend.repositories;

import java.util.List;
import java.util.Optional;

import dev.sajid.backend.models.normalized.course.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.student.StudentBatch;

public interface StudentBatchRepository extends JpaRepository<StudentBatch, Integer> {
    @Query("select distinct sb.semester from StudentBatch sb where sb.branch.id = :branchId")
    List<Integer> findSemestersByBranchId(@Param("branchId") int branchId);

    @Query("select distinct sb.section from StudentBatch sb where sb.branch.id = :branchId and sb.semester = :semester")
    List<String> findSectionsByBranchIdAndSemester(@Param("branchId") int branchId, @Param("semester") int semester);


    @Query("""
            select distinct sb.section from StudentBatch sb
            where sb.branch.branchCode = :branchCode
            and sb.semester = :semester
            """)
    List<String> findDistinctSectionsByBranchCodeAndSemester(@Param("branchCode") int branchCode,
                                                             @Param("semester") int semester);

    @Query("""
            select sb from StudentBatch sb
            where sb.branch.id = :branchId
            and sb.semester = :semester
            and sb.section = :section
            """)
    Optional<StudentBatch> findByBranch_IdAndSemesterAndSection(
            @Param("branchId") int branchId,
            @Param("semester") int semester,
            @Param("section") String section);


    @Query("""
            select sb from StudentBatch sb
            where sb.branch.branchCode = :branchCode
            and sb.semester = :semester
            and sb.section = :section
            """)
    Optional<StudentBatch> findByBranchCodeAndSemesterAndSection(
            @Param("branchCode")int branchCode,
            @Param("semester")int semester,
            @Param("section")String section);

    @Query("""
            select distinct sb.branch from StudentBatch sb
            where sb.branch.scheme.code = '20'
            order by sb.branch.branchCode
            """)
    List<Branch> findDistinctBranches();

    @Query("""
            select distinct sb.branch.branchCode from StudentBatch sb
            order by sb.branch.branchCode
            """)
    List<Integer> findDistinctBranchCodes();

    @Query("""
            select distinct semester from StudentBatch sb
            where sb.branch.branchCode = :branchCode
            """)
    List<Integer> findDistinctSemestersByBranchCode(@Param("branchCode") int branchCode);
}
