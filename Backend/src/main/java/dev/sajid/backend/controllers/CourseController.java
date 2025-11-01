package dev.sajid.backend.controllers;

import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.services.AttendanceService;
import dev.sajid.backend.services.CourseService;
import dev.sajid.backend.services.csv.RawCourseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.services.csv.CsvProcessingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


@Slf4j
@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {
    @Autowired
    CsvProcessingService csvProcessingService;

    @Autowired
    RawCourseProcessor rawCourseProcessor;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    AttendanceService attendanceService;


    @Autowired
    CourseService courseService;


    @PostMapping("/bulkupload")
    public ResponseEntity<?> courseBulkUpload(@RequestParam("file") MultipartFile file) {
        List<Course> rawCourses =csvProcessingService.process(file, Course.class);
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
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("The course doesn't exist with ID: " + courseId);
        }

        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(attendanceService.calculateAttendanceReportBetweenDates(courseId, startDate, endDate));
        }

        return ResponseEntity.ok(attendanceService.calculateFullAttendanceReportOfCourse(courseId));
    }

    @GetMapping("/{courseId}/startDate")
    public ResponseEntity<?> getCourseStartTime(@PathVariable("courseId") int courseId){
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("The course doesn't exist with ID: " + courseId);
        }
        return ResponseEntity.ok(Map.of(
                "startDate", courseService.getCourseStartDate(courseId)
        ));
    }

}
