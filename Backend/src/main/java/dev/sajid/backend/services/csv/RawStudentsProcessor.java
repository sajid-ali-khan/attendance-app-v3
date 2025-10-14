package dev.sajid.backend.services.csv;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import dev.sajid.backend.services.keys.BranchKey;
import dev.sajid.backend.services.keys.StudentBatchKey;
import org.springframework.stereotype.Service;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.models.raw.Student;
import dev.sajid.backend.repositories.BranchRepository;
import dev.sajid.backend.repositories.StudentBatchRepository;
import dev.sajid.backend.repositories.StudentRepository;
import jakarta.transaction.Transactional;

@Service
public class RawStudentsProcessor {

    private final StudentRepository studentRepository;
    private final StudentBatchRepository studentBatchRepository;
    private final BranchRepository branchRepository;

    public RawStudentsProcessor(StudentRepository studentRepository, StudentBatchRepository studentBatchRepository, BranchRepository branchRepository) {
        this.studentRepository = studentRepository;
        this.studentBatchRepository = studentBatchRepository;
        this.branchRepository = branchRepository;
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
                                new BranchKey(
                                        sb.getBranch().getScheme().getCode().trim().toUpperCase(),
                                        sb.getBranch().getBranchCode()
                                ),
                                sb.getSemester(),
                                sb.getSection().trim().toUpperCase()
                        ),
                        Function.identity()
                ));

        // Identify new batches from CSV
        List<StudentBatch> newBatches = rawStudents.stream()
                .map(rs -> {
                    String scheme = rs.getScheme().trim().toUpperCase();
                    int branchCode = Integer.parseInt(rs.getBranch().substring(0, 1));
                    int sem = rs.getSem();
                    String section = rs.getSection().trim().toUpperCase();
                    return new StudentBatchKey(new BranchKey(scheme, branchCode), sem, section);
                })
                .filter(key -> !existingBatches.containsKey(key))
                .map(key -> {
                    Branch branch = branchRepository.findBySchemeAndBranch(key.bkey().schemeCode(), key.bkey().branchCode())
                            .orElseThrow(() -> new RuntimeException("Branch not found for scheme: " + key.bkey().schemeCode() + " and branch code: " + key.bkey().branchCode()));
                    StudentBatch sb = new StudentBatch(0, branch, key.semester(), key.section(), null, null);
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
                    String scheme = rs.getScheme().trim().toUpperCase();

                    int branchCode = Integer.parseInt(rs.getBranch().substring(0, 1));

                    int sem = rs.getSem();

                    String section = rs.getSection().trim().toUpperCase();
                    
                    StudentBatchKey batchKey = new StudentBatchKey(new BranchKey(scheme, branchCode), sem, section);
                    
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
