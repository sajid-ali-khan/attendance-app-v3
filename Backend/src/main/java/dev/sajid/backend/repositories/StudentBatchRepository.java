package dev.sajid.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.student.StudentBatch;

public interface StudentBatchRepository extends JpaRepository<StudentBatch, Integer>{
    @Query("select distinct sb.semester from StudentBatch sb where sb.branch.branchCode = :branchCode")
    List<Integer> findSemestersByBranchCode(@Param("branchCode") String branchCode);

    @Query("select distinct sb.section from StudentBatch sb where sb.branch.branchCode = :branchCode and sb.semester = :semester")
    List<String> findSectionsByBranchCodeAndSemester(@Param("branchCode") String branchCode, @Param("semester") int semester);
}
