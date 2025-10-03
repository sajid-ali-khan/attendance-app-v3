package dev.sajid.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.course.BranchSubject;
import dev.sajid.backend.models.normalized.course.Subject;

public interface BranchSubjectRepository extends JpaRepository<BranchSubject, Integer>{
    
    @Query("select bs.subject from BranchSubject bs where bs.branch.branchCode = :branchCode and bs.semester = :semester")
    List<Subject> findSubjectsByBranchBranchCodeAndSemester(@Param("branchCode") String branchCode, @Param("semester") int semester);
}
