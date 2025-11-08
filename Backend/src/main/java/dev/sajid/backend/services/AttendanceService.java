package dev.sajid.backend.services;

import dev.sajid.backend.dtos.*;
import dev.sajid.backend.models.normalized.derived.AttendanceRecord;
import dev.sajid.backend.models.normalized.derived.Course;
import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.models.normalized.student.Student;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.repositories.SessionReporitory;
import dev.sajid.backend.repositories.StudentBatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AttendanceService {
    private final StudentBatchRepository studentBatchRepository;

    private final CourseRepository courseRepository;

    private final SessionReporitory sessionReporitory;

    public AttendanceService(StudentBatchRepository studentBatchRepository, CourseRepository courseRepository, SessionReporitory sessionReporitory) {
        this.studentBatchRepository = studentBatchRepository;
        this.courseRepository = courseRepository;
        this.sessionReporitory = sessionReporitory;
    }

    public FullAttendanceReport calculateFullAttendanceForStudentBatch(int studentBatchId){
        StudentBatch studentBatch = studentBatchRepository.findById(studentBatchId).get();

        if (studentBatch.getCourses().isEmpty())
            return null;

        FullAttendanceReport fullAttendanceReport = new FullAttendanceReport();
        fullAttendanceReport.setClassName(ClassNamingService.formClassName(studentBatch.getCourses().get(0)));
        Map<String, FullStudentAttendance> fullStudentAttendanceMap = new HashMap<>();

        for (Student student: studentBatch.getStudents()){
            FullStudentAttendance fsa = new FullStudentAttendance();
            fsa.setRoll(student.getRoll());
            fsa.setName(student.getName());
            fsa.setSubjectAttendanceMap(new HashMap<>());
            fullStudentAttendanceMap.put(student.getRoll(), fsa);
        }

        for (Course course: studentBatch.getCourses()){
            for (Session session: course.getSessions()){
                for (AttendanceRecord attendanceRecord: session.getAttendanceRecords()){
                    Student student = attendanceRecord.getStudent();
                    if (!fullStudentAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().containsKey(course.getBranchSubject().getSubject().getId())){
                        SubjectAttendance ca = new SubjectAttendance();
                        ca.setSubjectName(ClassNamingService.formSubjectShortName(course));
                        ca.setSubjectId(course.getBranchSubject().getSubject().getId());
                        fullStudentAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().put(course.getBranchSubject().getSubject().getId(), ca);
                    }
                    SubjectAttendance ca = fullStudentAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().get(course.getBranchSubject().getSubject().getId());
                    if (attendanceRecord.isStatus())
                        ca.daysPresent += 1;
                    ca.totalDays += 1;
                    fullStudentAttendanceMap.get(student.getRoll()).getSubjectAttendanceMap().put(course.getBranchSubject().getSubject().getId(), ca);
                }
            }
        }

        for (Map.Entry<String, FullStudentAttendance> entry: fullStudentAttendanceMap.entrySet()){
            int presentDays = 0;
            int totalDays = 0;
            for (Map.Entry<Integer, SubjectAttendance> entry1: entry.getValue().getSubjectAttendanceMap().entrySet()){
                int _presentDays = entry1.getValue().daysPresent;
                int _totalDays = entry1.getValue().totalDays;

                totalDays += _totalDays;
                presentDays += _presentDays;

                fullStudentAttendanceMap.get(entry.getKey()).getSubjectAttendanceMap().get(entry1.getKey()).setPercentage(
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
            fullStudentAttendanceMap.get(entry.getKey()).getSubjectAttendanceMap().put(-1, ca);
        }

        fullAttendanceReport.setFullStudentAttendanceMap(new TreeMap<>(fullStudentAttendanceMap));
        return fullAttendanceReport;
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
