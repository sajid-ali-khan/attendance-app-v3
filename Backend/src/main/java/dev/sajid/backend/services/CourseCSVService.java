// File: src/main/java/dev/sajid/backend/services/CourseCSVService.java

package dev.sajid.backend.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import dev.sajid.backend.exceptions.CsvValidationException;
import dev.sajid.backend.models.raw.Course;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CourseCSVService {

    /**
     * Processes an uploaded CSV file, validates its headers and content,
     * and maps the rows to a list of Course objects.
     *
     * @param file The CSV file uploaded by the user.
     * @return A list of Course objects parsed from the file.
     * @throws CsvValidationException if headers are incorrect, fields are missing,
     * or the file is malformed.
     */
    public List<Course> process(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CsvValidationException("The uploaded file is empty.");
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            // This builder configures the CSV parser.
            CsvToBean<Course> csvToBean = new CsvToBeanBuilder<Course>(reader)
                    .withType(Course.class) // The target class for mapping
                    .withIgnoreLeadingWhiteSpace(true) // Trims whitespace
                    .withThrowExceptions(true) // This is key for validation
                    .build();

            // The .parse() method does all the magic:
            // 1. It verifies that headers like 'course_code', 'course_name' exist.
            // 2. It checks the 'required=true' constraint on each field.
            // 3. It maps all valid rows to Course objects.
            List<Course> courses = csvToBean.parse();

            if (courses.isEmpty()) {
                throw new CsvValidationException("The CSV file contains no data rows.");
            }

            return courses;

        } catch (Exception ex) {
            // This block catches various potentional errors.
            // We wrap them in our custom exception for consistent handling upstream.
            // For example, CsvRequiredFieldEmptyException is thrown if a required field is missing.
            // A generic Exception can be thrown for header mismatches.
            throw new CsvValidationException("Failed to process CSV file: " + ex.getMessage(), ex);
        }
    }
}