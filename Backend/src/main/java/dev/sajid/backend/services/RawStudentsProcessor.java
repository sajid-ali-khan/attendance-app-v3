package dev.sajid.backend.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.sajid.backend.models.normalized.course.Degree;
import dev.sajid.backend.models.normalized.course.Program;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.models.raw.Student;
import dev.sajid.backend.repositories.ProgramRepository;
import dev.sajid.backend.repositories.StudentBatchRepository;
import dev.sajid.backend.repositories.StudentRepository;
import jakarta.transaction.Transactional;

@Service
public class RawStudentsProcessor {

    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;
    private final StudentBatchRepository studentBatchRepository;

    public RawStudentsProcessor(StudentRepository studentRepository, ProgramRepository programRepository,
            StudentBatchRepository studentBatchRepository) {
        this.studentRepository = studentRepository;
        this.programRepository = programRepository;
        this.studentBatchRepository = studentBatchRepository;
    }

    @Transactional
    public void process(List<Student> rawStudents) {

        // Normalize and create batch keys
        Map<StudentBatchKey, StudentBatch> studentBatchesMap = findOrCreateStudentBatches(rawStudents);

        // Process students
        findOrCreateStudents(rawStudents, studentBatchesMap);
    }

    private Map<StudentBatchKey, StudentBatch> findOrCreateStudentBatches(List<Student> rawStudents) {
        // Fetch all existing batches
        Map<StudentBatchKey, StudentBatch> existingBatches = studentBatchRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        sb -> new StudentBatchKey(
                                sb.getProgram().getDegree(),
                                sb.getProgram().getScheme().getCode().trim().toUpperCase(),
                                sb.getProgram().getBranch().getBranchCode(),
                                sb.getSemester(),
                                sb.getSection().trim().toUpperCase()
                        ),
                        Function.identity()
                ));

        // Identify new batches from CSV
        List<StudentBatch> newBatches = rawStudents.stream()
                .map(rs -> {
                    Degree degree = rs.getDegree().startsWith("B") ? Degree.BTech : Degree.MTech;
                    String scheme = rs.getScheme().trim().toUpperCase();
                    int branchCode = Integer.parseInt(rs.getBranch().substring(0, 1));
                    int sem = rs.getSem();
                    String section = rs.getSection().trim().toUpperCase();
                    return new StudentBatchKey(degree, scheme, branchCode, sem, section);
                })
                .filter(key -> !existingBatches.containsKey(key))
                .map(key -> {
                    Program program = programRepository
                            .findBySchemeAndBranch(key.schemeCode(), key.branchCode())
                            .orElseThrow(() -> new RuntimeException(
                                    "Program not found for scheme " + key.schemeCode() + " and branch " + key.branchCode()
                            ));
                    StudentBatch sb = new StudentBatch(0, program, key.semester(), key.section(), null, null);
                    existingBatches.put(key, sb);
                    return sb;
                })
                .collect(Collectors.toList());

        if (!newBatches.isEmpty()) {
            studentBatchRepository.saveAll(newBatches);
        }

        return existingBatches;
    }

    private void findOrCreateStudents(List<Student> rawStudents, Map<StudentBatchKey, StudentBatch> studentBatchesMap) {
        // Fetch all existing students by roll (normalized)
        Map<String, dev.sajid.backend.models.normalized.student.Student> existingStudents = studentRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        s -> s.getRoll().trim().toUpperCase(),
                        Function.identity()
                ));

        List<dev.sajid.backend.models.normalized.student.Student> newStudents = rawStudents.stream()
                .map(rs -> {
                    String roll = rs.getRoll().trim().toUpperCase();
                    if (existingStudents.containsKey(roll)) return null; // Skip existing
                    Degree degree = rs.getDegree().startsWith("B") ? Degree.BTech : Degree.MTech;
                    String scheme = rs.getScheme().trim().toUpperCase();
                    int branchCode = Integer.parseInt(rs.getBranch().substring(0, 1));
                    int sem = rs.getSem();
                    String section = rs.getSection().trim().toUpperCase();
                    StudentBatchKey batchKey = new StudentBatchKey(degree, scheme, branchCode, sem, section);
                    StudentBatch studentBatch = studentBatchesMap.get(batchKey);
                    dev.sajid.backend.models.normalized.student.Student s =
                            new dev.sajid.backend.models.normalized.student.Student(0, roll, rs.getName(), studentBatch, null);
                    existingStudents.put(roll, s); // mark as existing
                    return s;
                })
                .filter(s -> s != null)
                .collect(Collectors.toList());

        if (!newStudents.isEmpty()) {
            studentRepository.saveAll(newStudents);
        }
    }
}
