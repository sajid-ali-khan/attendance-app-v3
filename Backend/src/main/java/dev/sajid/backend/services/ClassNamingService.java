package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.derived.Course;
import dev.sajid.backend.models.normalized.student.StudentBatch;

public class ClassNamingService {
    public static String formClassName(Course course){

        if (course == null) return "Class name";
        return formClassNameFromStudentBatch(course.getStudentBatch());
    }

    public static String formClassNameFromStudentBatch(StudentBatch studentBatch){
        if (studentBatch == null) return "Class name";

        String branchName = studentBatch.getBranch().getShortForm();
        int semester = studentBatch.getSemester();
        String section = studentBatch.getSection();

        return String.format(
                "%s%s%s", branchName, semester, section
        );
    }

    public static String formSubjectName(Course course){
        String subjectFullName = course.getBranchSubject().getSubject().getFullForm();
        String subjectCode = course.getBranchSubject().getSubject().getShortForm();

        return subjectCode + " - " + subjectFullName;
    }

    public static String formSubjectShortName(Course course){
        return course.getBranchSubject().getSubject().getShortForm();
    }


    private static String formatedSemester(int sem){
        return sem + switch(sem) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}
