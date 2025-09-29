package dev.sajid.backend.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.sajid.backend.models.normalized.course.Degree;
import dev.sajid.backend.models.normalized.course.Program;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.models.raw.Student;
import dev.sajid.backend.repositories.BranchRepository;
import dev.sajid.backend.repositories.ProgramRepository;
import dev.sajid.backend.repositories.StudentBatchRepository;
import dev.sajid.backend.repositories.StudentRepository;

public class RawStudentsProcessor {

    final StudentRepository studentRepository;
    final ProgramRepository programRepository;
    final BranchRepository branchRepository;
    private StudentBatchRepository studentBatchRepository;

    RawStudentsProcessor(StudentRepository studentRepository, ProgramRepository programRepository,
            BranchRepository branchRepository, StudentBatchRepository studentBatchRepository) {
        this.studentRepository = studentRepository;
        this.programRepository = programRepository;
        this.branchRepository = branchRepository;
        this.studentBatchRepository = studentBatchRepository;
    }

    void process(List<Student> rawStudents) {
        Map<StudentBatchKey, StudentBatch> studentBatchesMap = findOrCreateStudentBatches(rawStudents);
        findOrCreateStudents(rawStudents, studentBatchesMap);
    }

    Map<StudentBatchKey, StudentBatch> findOrCreateStudentBatches(List<Student> rawStudents) {
        Map<StudentBatchKey, StudentBatch> existingStudentBatches = studentBatchRepository.findAll()
                .stream()
                .collect(Collectors.toMap(sb -> new StudentBatchKey(sb.getProgram().getDegree(),
                        sb.getProgram().getScheme().getCode(), sb.getProgram().getBranch().getBranchCode(),
                        sb.getSemester(), sb.getSection()),
                        Function.identity()));

        List<StudentBatch> newStudentBatches = rawStudents.stream()
                .map(rs -> {
                    Degree degree = rs.getDegree().startsWith("B") ? Degree.BTech : Degree.MTech;
                    int branchCode = Integer.parseInt(rs.getBranch().substring(0, 1));
                    return new StudentBatchKey(degree, rs.getScheme(), branchCode, rs.getSem(), rs.getSection());
                })
                .filter(key -> !existingStudentBatches.containsKey(key))
                .map(key -> {
                    Program program = programRepository
                            .findBySchemeAndBranch(key.schemeCode(), key.branchCode()).get();
                    return new StudentBatch(0, program, key.semester(), key.section(), null, null);
                })
                .collect(Collectors.toList());

        if (!newStudentBatches.isEmpty()) {
            studentBatchRepository.saveAll(newStudentBatches);
            newStudentBatches.forEach(sb -> existingStudentBatches.put(new StudentBatchKey(sb.getProgram().getDegree(),
                    sb.getProgram().getScheme().getCode(), sb.getProgram().getBranch().getBranchCode(),
                    sb.getSemester(), sb.getSection()), sb));
        }

        return existingStudentBatches;
    }

    void findOrCreateStudents(List<Student> rawStudents, Map<StudentBatchKey, StudentBatch> studentBatchesMap) {
        Map<String, dev.sajid.backend.models.normalized.student.Student> existingStudents = studentRepository.findAll()
                .stream()
                .collect(Collectors.toMap(s -> s.getRoll(), Function.identity()));

        List<dev.sajid.backend.models.normalized.student.Student> newStudents = rawStudents.stream()
                .filter(rs -> !existingStudents.containsKey(rs.getRoll()))
                .map(rs -> {
                    Degree degree = rs.getDegree().startsWith("B") ? Degree.BTech : Degree.MTech;
                    int branchCode = Integer.parseInt(rs.getBranch().substring(0, 1));
                    StudentBatchKey batchKey = new StudentBatchKey(degree, rs.getScheme(), branchCode, rs.getSem(),
                            rs.getSection());
                    StudentBatch studentBatch = studentBatchesMap.get(batchKey);
                    return new dev.sajid.backend.models.normalized.student.Student(0, rs.getRoll(), rs.getName(),
                            studentBatch, null);
                })
                .collect(Collectors.toList());
        if (!newStudents.isEmpty()) {
            studentRepository.saveAll(newStudents);
            newStudents.forEach(s -> existingStudents.put(s.getRoll(), s));
        }
    }
}
