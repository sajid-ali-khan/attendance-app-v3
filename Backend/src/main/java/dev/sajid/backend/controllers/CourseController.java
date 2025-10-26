package dev.sajid.backend.controllers;

import dev.sajid.backend.services.csv.RawCourseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.services.csv.CsvProcessingService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {
    @Autowired
    CsvProcessingService csvProcessingService;

    @Autowired
    RawCourseProcessor rawCourseProcessor;


    @PostMapping("/bulkupload")
    public ResponseEntity<?> courseBulkUpload(@RequestParam("file") MultipartFile file) {
        List<Course> rawCourses =csvProcessingService.process(file, Course.class);
        rawCourseProcessor.processRawCourses(rawCourses);
        return ResponseEntity.ok().body(Map.of(
                "message", "Courses uploaded successfully"
        ));
    }
    
}
