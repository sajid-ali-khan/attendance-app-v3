package dev.sajid.backend.services;

import dev.sajid.backend.dtos.AttendanceReport;
import dev.sajid.backend.dtos.StudentAttendance;
import dev.sajid.backend.models.normalized.derived.AttendanceRecord;
import dev.sajid.backend.models.normalized.derived.Course;
import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.models.normalized.student.Student;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.repositories.SessionReporitory;
import dev.sajid.backend.repositories.StudentBatchRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AttendanceService {
    @Autowired
    private StudentBatchRepository studentBatchRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SessionReporitory sessionReporitory;

    @Data
    public static class AttendanceAggregate{
        int presentDays;
        int totalDays;
        double percentage;
    }

    public Map<String, Map<Integer, AttendanceAggregate>> calculateFullAttendanceForStudentBatch(int studentBatchId){
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

    public AttendanceReport calculateAttendanceReportBetweenDates(int courseId, LocalDate startDate, LocalDate endTime){
        Course course = courseRepository.findById(courseId).get();

        List<Session> sessions = sessionReporitory.findByCourse_IdAndTimeStampBetween(courseId, startDate.atStartOfDay(), endTime.plusDays(1).atStartOfDay());

        return calculateAttendanceReport(sessions, course);
    }


    public AttendanceReport calculateFullAttendanceReportOfCourse(int courseId){
        Course course = courseRepository.findById(courseId).get();

        return calculateAttendanceReport(course.getSessions(), course);
    }

    private AttendanceReport calculateAttendanceReport(List<Session> sessions, Course course){
        AttendanceReport attendanceReport = new AttendanceReport();
        String className = ClassNamingService.formClassName(course);
        String subjectName = ClassNamingService.formSubjectName(course);
        attendanceReport.setClassName(className);
        attendanceReport.setSubjectName(subjectName);

        Map<String, StudentAttendance> studentAttendanceMap = new HashMap<>();

        for (Session session: sessions){
            for (AttendanceRecord attendanceRecord: session.getAttendanceRecords()){
                Student student = attendanceRecord.getStudent();
                if (!studentAttendanceMap.containsKey(student.getRoll())){
                    studentAttendanceMap.put(student.getRoll(), new StudentAttendance());
                    studentAttendanceMap.get(student.getRoll()).setRoll(student.getRoll());
                    studentAttendanceMap.get(student.getRoll()).setName(student.getName());
                }
                studentAttendanceMap.get(student.getRoll()).totalDays += 1;
                if (attendanceRecord.isStatus()){
                    studentAttendanceMap.get(student.getRoll()).presentDays += 1;
                }
            }
        }

        for (Map.Entry<String, StudentAttendance> entry: studentAttendanceMap.entrySet()){
            if (entry.getValue().totalDays == 0){
                continue;
            }
            double percentage = 100 * (double) entry.getValue().presentDays / entry.getValue().totalDays;
            studentAttendanceMap.get(entry.getKey()).setAttendancePercentage(percentage);
        }
        attendanceReport.setStudentAttendanceMap(new TreeMap<>(studentAttendanceMap));

        return attendanceReport;
    }
}
