package dev.sajid.backend.controllers;

import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.services.AttendanceService;
import dev.sajid.backend.services.csv.RawCourseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.services.csv.CsvProcessingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;


@Slf4j
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CsvProcessingService csvProcessingService;
    private final RawCourseProcessor rawCourseProcessor;
    private final CourseRepository courseRepository;
    private final AttendanceService attendanceService;

    public CourseController(CsvProcessingService csvProcessingService, RawCourseProcessor rawCourseProcessor, CourseRepository courseRepository, AttendanceService attendanceService) {
        this.csvProcessingService = csvProcessingService;
        this.rawCourseProcessor = rawCourseProcessor;
        this.courseRepository = courseRepository;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/bulkupload")
    public ResponseEntity<?> courseBulkUpload(@RequestParam("file") MultipartFile file) {
        List<Course> rawCourses = csvProcessingService.process(file, Course.class);
        rawCourseProcessor.processRawCourses(rawCourses);
        return ResponseEntity.ok().body(Map.of(
                "message", "Courses uploaded successfully"
        ));
    }

    @GetMapping("/{courseId}/attendanceReport")
    public ResponseEntity<?> getCourseAttendanceReport(
            @PathVariable int courseId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        checkCourseExistence(courseId);

        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(attendanceService.calculateAttendanceReportBetweenDates(courseId, startDate, endDate));
        }

        return ResponseEntity.ok(attendanceService.calculateFullAttendanceReportOfCourse(courseId));
    }

    private void checkCourseExistence(int courseId){
        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
    }

}
