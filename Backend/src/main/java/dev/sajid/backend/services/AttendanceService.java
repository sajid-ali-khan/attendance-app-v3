package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.derived.AttendanceRecord;
import dev.sajid.backend.models.normalized.derived.Course;
import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.models.normalized.student.Student;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.StudentBatchRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AttendanceService {
    @Autowired
    private StudentBatchRepository studentBatchRepository;

    @Data
    public static class AttendanceAggregate{
        int presentDays;
        int totalDays;
        double percentage;
    }

    public Map<String, Map<Integer, AttendanceAggregate>> calculateAttendanceForStudentBatch(int studentBatchId){
        StudentBatch studentBatch = studentBatchRepository.findById(studentBatchId).get();

        Map<String, Map<Integer, AttendanceAggregate>> attendanceMap = new HashMap<>();

        for (Student student: studentBatch.getStudents()){
            attendanceMap.put(student.getRoll(), new HashMap<>());
        }

        for (Course course: studentBatch.getCourses()){
            for (Session session: course.getSessions()){
                for (AttendanceRecord attendanceRecord: session.getAttendanceRecords()){
                    Student student = attendanceRecord.getStudent();
                    if (!attendanceMap.get(student.getRoll()).containsKey(course.getId())){
                        attendanceMap.get(student.getRoll()).put(course.getId(), new AttendanceAggregate());
                    }
                    if (attendanceRecord.isStatus()) {
                        attendanceMap.get(student.getRoll()).get(course.getId()).presentDays += 1;
                    }
                    attendanceMap.get(student.getRoll()).get(course.getId()).totalDays += 1;
                }
            }
        }
        return attendanceMap;
    }
}
