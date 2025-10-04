package dev.sajid.backend.dtos;

import java.util.List;

public record ViewAssignmentDto(
        String subjectCode,//short form
        String subject,
        String subjectType,
        List<FacultyDto> faculties
) { }
// { 
//     id: 1,
//      scheme: '2021-25',
//       branch: 'Computer Science',
//        semester: '4',
//         section: 'A',
//         subject: 'Data Structures',
//          faculty: 'Dr. Alan Turing' }