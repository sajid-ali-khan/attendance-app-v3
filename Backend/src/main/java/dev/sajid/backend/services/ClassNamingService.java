package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.derived.Course;

public class ClassNamingService {
    public static String formClassName(Course course){

        String branchName = course.getBranchSubject().getBranch().getShortForm();
        int semester = course.getStudentBatch().getSemester();
        String section = course.getStudentBatch().getSection();

        return String.format(
                "%s Sem %s - %s",
                formatedSemester(semester),
                branchName,
                section
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
