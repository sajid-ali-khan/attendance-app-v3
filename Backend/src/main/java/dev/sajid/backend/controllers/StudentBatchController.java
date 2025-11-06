package dev.sajid.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.sajid.backend.dtos.SubjectDto;
import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.models.normalized.course.Subject;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.BranchRepository;
import dev.sajid.backend.services.AttendanceService;
import dev.sajid.backend.services.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.sajid.backend.repositories.StudentBatchRepository;

import static java.rmi.server.LogStream.log;


@RestController
@RequestMapping("/api/student-batches")
@CrossOrigin
@Slf4j
public class StudentBatchController {
    private final StudentBatchRepository studentBatchRepository;

    private final AttendanceService attendanceService;

    private final BranchRepository branchRepository;
    private final BranchService branchService;

    public StudentBatchController(StudentBatchRepository studentBatchRepository, AttendanceService attendanceService, BranchRepository branchRepository, BranchService branchService) {
        this.studentBatchRepository = studentBatchRepository;
        this.attendanceService = attendanceService;
        this.branchRepository = branchRepository;
        this.branchService = branchService;
    }

    @GetMapping("/branches")
    public ResponseEntity<?> getBranches() {
        return ResponseEntity.ok(branchService.getDistinctBranches());
    }

    @GetMapping("/semesters")
    public ResponseEntity<?> getSemestersByBranchId(
            @RequestParam(value = "branchId") Integer branchId
    ) {
        checkBranchExistence(branchId);
        List<Integer> semesters = studentBatchRepository.findSemestersByBranchId(branchId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/sections")
    public ResponseEntity<?> getSectionsBySemesterAndBranchId(
            @RequestParam("branchId") Integer branchId,
            @RequestParam("semester") Integer semester
    ) {
        checkBranchExistence(branchId);
        List<String> sections = studentBatchRepository.findSectionsByBranchIdAndSemester(branchId, semester);
        return ResponseEntity.ok(sections);
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjectsByBranchAndSemesterAndSection(
            @RequestParam("branchId") Integer branchId,
            @RequestParam("semester") Integer semester,
            @RequestParam("section") String section
    ) {
        log.debug("BranchId = {}, semester = {}, section = {}", branchId, semester, section);
        Optional<StudentBatch> studentBatchOptional = studentBatchRepository.findByBranch_IdAndSemesterAndSection(branchId, semester, section);

        if (studentBatchOptional.isEmpty()) {
            throw new ResourceNotFoundException("StudentBatch with doesn't exist for given branch, semester and section");
        }
        List<Subject> subjects = studentBatchOptional.get().getCourses().stream()
                .map(c -> c.getBranchSubject().getSubject()).toList();
        List<SubjectDto> subjectDtos = subjects.stream().map(sub -> new SubjectDto(sub.getId(), sub.getShortForm(), sub.getFullForm())).toList();
        return ResponseEntity.ok(subjectDtos);
    }

    @GetMapping("/report")
    public ResponseEntity<?> getFullAttendance(
            @RequestParam("branchId") Integer branchId,
            @RequestParam("semester") Integer semester,
            @RequestParam("section") String section
    ) {
        log.debug("BranchId = {}, semester = {}, section = {}", branchId, semester, section);
        Optional<StudentBatch> studentBatch = studentBatchRepository.findByBranch_IdAndSemesterAndSection(branchId, semester, section);
        if (studentBatch.isEmpty()){
            throw new ResourceNotFoundException("StudentBatch with doesn't exist for given branch, semester and section");
        }
        return ResponseEntity.ok(attendanceService.calculateFullAttendanceForStudentBatch(studentBatch.get().getId()));
    }

    private void checkBranchExistence(int branchId) {
        if (!branchRepository.existsById(branchId))
            throw new ResourceNotFoundException("Branch not found with ID: " + branchId);
    }

    private void checkStudentBatchExistence(int studentBatchId) {
        if (!studentBatchRepository.existsById(studentBatchId))
            throw new ResourceNotFoundException("Student Batch not found with ID: " + studentBatchId);
    }
}
