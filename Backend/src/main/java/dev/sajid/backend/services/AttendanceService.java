package dev.sajid.backend.services;

import dev.sajid.backend.dtos.*;
import dev.sajid.backend.models.normalized.derived.AttendanceRecord;
import dev.sajid.backend.models.normalized.derived.Course;
import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.models.normalized.student.Student;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.repositories.SessionRepository;
import dev.sajid.backend.repositories.StudentBatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class AttendanceService {
    private final StudentBatchRepository studentBatchRepository;

    private final CourseRepository courseRepository;

    private final SessionRepository sessionRepository;


    private final ZoneId zone = ZoneId.of("Asia/Kolkata");

    public AttendanceService(StudentBatchRepository studentBatchRepository, CourseRepository courseRepository, SessionRepository sessionRepository) {
        this.studentBatchRepository = studentBatchRepository;
        this.courseRepository = courseRepository;
        this.sessionRepository = sessionRepository;
    }

    public Map<String, StudentSubjectsAttendance> calculateFullAttendanceForStudentBatch(int studentBatchId){
        StudentBatch studentBatch = studentBatchRepository.findById(studentBatchId).get();

        if (studentBatch.getCourses().isEmpty())
            return null;

        Map<String, StudentSubjectsAttendance> multipleStudentsSubjectsAttendanceMap = new HashMap<>();

        for (Student student: studentBatch.getStudents()){
            StudentSubjectsAttendance fsa = new StudentSubjectsAttendance();
            fsa.setRoll(student.getRoll());
            fsa.setName(student.getName());
            fsa.setSubjectAttendanceMap(new HashMap<>());
            multipleStudentsSubjectsAttendanceMap.put(student.getRoll(), fsa);
        }

        // counting present days for each session of each course of each student
        for (Course course: studentBatch.getCourses()){
            for (Session session: course.getSessions()){
                for (AttendanceRecord attendanceRecord: session.getAttendanceRecords()){
                    Student student = attendanceRecord.getStudent();
                    if (!multipleStudentsSubjectsAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().containsKey(course.getBranchSubject().getSubject().getId())){
                        SubjectAttendance ca = new SubjectAttendance();
                        ca.setSubjectName(ClassNamingService.formSubjectShortName(course));
                        ca.setSubjectId(course.getBranchSubject().getSubject().getId());
                        multipleStudentsSubjectsAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().put(course.getBranchSubject().getSubject().getId(), ca);
                    }
                    SubjectAttendance ca = multipleStudentsSubjectsAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().get(course.getBranchSubject().getSubject().getId());
                    if (attendanceRecord.isStatus())
                        ca.daysPresent += 1;
                    ca.totalDays += 1;
                    multipleStudentsSubjectsAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().put(course.getBranchSubject().getSubject().getId(), ca);
                }
            }
        }

        for (Map.Entry<String, StudentSubjectsAttendance> entry: multipleStudentsSubjectsAttendanceMap.entrySet()){
            int presentDays = 0;
            int totalDays = 0;
            for (Map.Entry<Integer, SubjectAttendance> entry1: entry.getValue().getSubjectAttendanceMap().entrySet()){
                int _presentDays = entry1.getValue().daysPresent;
                int _totalDays = entry1.getValue().totalDays;

                totalDays += _totalDays;
                presentDays += _presentDays;

                multipleStudentsSubjectsAttendanceMap.get(entry.getKey()).getSubjectAttendanceMap().get(entry1.getKey()).setPercentage(
                        (double)_presentDays * 100 / _totalDays
                );
            }
            double total = (double) presentDays * 100 / totalDays;
            SubjectAttendance ca = new SubjectAttendance();
            ca.setPercentage(total);
            ca.setDaysPresent(presentDays);
            ca.setTotalDays(totalDays);
            ca.setSubjectName("Total");
            ca.setSubjectId(-1);
            multipleStudentsSubjectsAttendanceMap.get(entry.getKey()).getSubjectAttendanceMap().put(-1, ca);
        }

        return new TreeMap<>(multipleStudentsSubjectsAttendanceMap);
    }


    public AttendanceReport calculateConsolidatedAttendanceReport(
            int branchCode,
            int semester,
            String section,
            LocalDate startDate,
            LocalDate endDate){
        Optional<StudentBatch> studentBatch = studentBatchRepository.findByBranchCodeAndSemesterAndSection(branchCode, semester, section);

        if (studentBatch.isEmpty()){
            log.debug("Student batch not found for branchCode: {}, semester: {}, section: {}", branchCode, semester, section);
            return null;
        }

        List<Course> courses = studentBatch.get().getCourses();

        AttendanceReport attendanceReport = new AttendanceReport();
//        String className = ClassNamingService.formClassName(courses.getFirst());
        attendanceReport.setClassName("X class");
        attendanceReport.setSubjectName("Total");
        attendanceReport.setStudentAttendanceMap(new HashMap<>());

        for (Course course: courses){
            List<Session> sessions;
            if (startDate != null && endDate != null){
                Instant start = startDate.atStartOfDay(zone).toInstant();
                Instant end = endDate.plusDays(1).atStartOfDay(zone).toInstant();
                sessions = sessionRepository.findByCourse_IdAndCreatedAtBetween(course.getId(), start, end);
            }else{
                sessions = course.getSessions();
            }
            calculateAttendanceReport(sessions, attendanceReport);
        }
        return attendanceReport;
    }

    public AttendanceReport calculateAttendanceReportBetweenDates(int courseId, LocalDate startDate, LocalDate endDate){
        Course course = courseRepository.findById(courseId).get();
        Instant start = startDate.atStartOfDay(zone).toInstant();
        Instant end = endDate.plusDays(1).atStartOfDay(zone).toInstant();

        List<Session> sessions = sessionRepository.findByCourse_IdAndCreatedAtBetween(courseId, start, end);

        AttendanceReport attendanceReport = new AttendanceReport();
        String className = ClassNamingService.formClassName(course);
        String subjectName = ClassNamingService.formSubjectName(course);
        attendanceReport.setClassName(className);
        attendanceReport.setSubjectName(subjectName);
        attendanceReport.setStudentAttendanceMap(new HashMap<>());
        calculateAttendanceReport(sessions, attendanceReport);
        return attendanceReport;
    }


    public AttendanceReport calculateFullAttendanceReportOfCourse(int courseId){
        Course course = courseRepository.findById(courseId).get();

        AttendanceReport attendanceReport = new AttendanceReport();
        String className = ClassNamingService.formClassName(course);
        String subjectName = ClassNamingService.formSubjectName(course);
        attendanceReport.setClassName(className);
        attendanceReport.setSubjectName(subjectName);
        attendanceReport.setStudentAttendanceMap(new HashMap<>());
        calculateAttendanceReport(course.getSessions(), attendanceReport);
        return attendanceReport;
    }

    private void calculateAttendanceReport(List<Session> sessions, AttendanceReport attendanceReport){
        Map<String, StudentAttendance> studentAttendanceMap = attendanceReport.getStudentAttendanceMap();

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
    }
}
