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
        return ResponseEntity.ok(branchService.getDistinctBranchesByBranchCodes());
    }

    @GetMapping("/semesters")
    public ResponseEntity<?> getSemestersByBranchId(
            @RequestParam(value = "branchId", required = false) Integer branchId,
            @RequestParam(value = "branchCode", required = false) Integer branchCode
    ) {
        if (branchCode == null && branchId == null){
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Provide either branchId or branchCode."
            ));
        }
        if (branchId != null){
            checkBranchExistence(branchId);
            List<Integer> semesters = studentBatchRepository.findSemestersByBranchId(branchId);
            return ResponseEntity.ok(semesters);
        }
        List<Integer> semesters = studentBatchRepository.findDistinctSemestersByBranchCode(branchCode);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/sections")
    public ResponseEntity<?> getSectionsBySemesterAndBranchId(
            @RequestParam(value = "branchId", required = false) Integer branchId,
            @RequestParam(value = "branchCode", required = false) Integer branchCode,
            @RequestParam("semester") Integer semester
    ) {
        if (branchCode == null && branchId == null){
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Provide either branchId or branchCode."
            ));
        }
        if (branchId != null){
            checkBranchExistence(branchId);
            List<String> sections = studentBatchRepository.findSectionsByBranchIdAndSemester(branchId, semester);
            return ResponseEntity.ok(sections);
        }
        List<String> sections = studentBatchRepository.findDistinctSectionsByBranchCodeAndSemester(branchCode, semester);
        return ResponseEntity.ok(sections);

    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjectsByBranchAndSemesterAndSection(
            @RequestParam(value = "branchId", required = false) Integer branchId,
            @RequestParam(value = "branchCode", required = false) Integer branchCode,
            @RequestParam("semester") Integer semester,
            @RequestParam("section") String section
    ) {
        if (branchCode == null && branchId == null){
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Provide either branchId or branchCode."
            ));
        }
        log.debug("BranchId = {}, semester = {}, section = {}", branchId, semester, section);

        Optional<StudentBatch> studentBatchOptional;
        if (branchId != null){
            studentBatchOptional = studentBatchRepository.findByBranch_IdAndSemesterAndSection(branchId, semester, section);
        }else {
            studentBatchOptional = studentBatchRepository.findByBranchCodeAndSemesterAndSection(branchCode, semester, section);
        }
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
            @RequestParam(value = "branchId", required = false) Integer branchId,
            @RequestParam(value = "branchCode", required = false) Integer branchCode,
            @RequestParam("semester") Integer semester,
            @RequestParam("section") String section
    ) {
        if (branchCode == null && branchId == null){
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Provide either branchId or branchCode."
            ));
        }
        log.debug("BranchId = {}, semester = {}, section = {}", branchId, semester, section);

        Optional<StudentBatch> studentBatch;
        if (branchId != null){
            studentBatch = studentBatchRepository.findByBranch_IdAndSemesterAndSection(branchId, semester, section);
        }else {
            studentBatch = studentBatchRepository.findByBranchCodeAndSemesterAndSection(branchCode, semester, section);
        }
        if (studentBatch.isEmpty()) {
            throw new ResourceNotFoundException("StudentBatch with doesn't exist for given branch, semester and section");
        }
        log.debug("BranchId = {}, semester = {}, section = {}", branchId, semester, section);

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
