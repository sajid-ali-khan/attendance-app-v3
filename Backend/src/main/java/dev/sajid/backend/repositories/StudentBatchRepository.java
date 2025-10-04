package dev.sajid.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.student.StudentBatch;

public interface StudentBatchRepository extends JpaRepository<StudentBatch, Integer>{
    @Query("select distinct sb.semester from StudentBatch sb where sb.branch.id = :branchId")
    List<Integer> findSemestersByBranchId(@Param("branchId") int branchId);

    @Query("select distinct sb.section from StudentBatch sb where sb.branch.id = :branchId and sb.semester = :semester")
    List<String> findSectionsByBranchIdAndSemester(@Param("branchId") int branchId, @Param("semester") int semester);


    @Query(value = """
            select * from student_batches sb
            where sb.branch_id = :branchId
            and sb.semester = :semester
            and sb.section = :section
            limit 1
            """, nativeQuery = true)
    Optional<StudentBatch> findFirstByBranchIdAndSemesterAndSection(int branchId, int semester, String section);
}
