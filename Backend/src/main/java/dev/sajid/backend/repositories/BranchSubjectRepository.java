package dev.sajid.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.course.BranchSubject;
import dev.sajid.backend.models.normalized.course.Subject;

public interface BranchSubjectRepository extends JpaRepository<BranchSubject, Integer>{
    
    @Query("select bs.subject from BranchSubject bs where bs.branch.branchCode = :branchCode and bs.semester = :semester")
    List<Subject> findSubjectsByBranchBranchCodeAndSemester(@Param("branchCode") String branchCode, @Param("semester") int semester);

    @Query("select bs from BranchSubject bs where bs.branch.id = :branchId and bs.subject.id = :subjectId and bs.semester = :semester")
    Optional<BranchSubject> findFirstByBranchIdAndSubjectIdAndSemester(@Param("branchId") int branchId, @Param("subjectId") int subjectId, @Param("semester") int semester);
}
