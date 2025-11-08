package dev.sajid.backend.services.csv;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.normalized.faculty.FacultyRole;
import dev.sajid.backend.models.raw.Employee;
import dev.sajid.backend.repositories.FacultyRepository;

@Service
public class RawEmployeeProcessor {
    private final FacultyRepository facultyRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public RawEmployeeProcessor(FacultyRepository facultyRepository, BCryptPasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void processEmployees(List<Employee> rawEmployees) {
        // fetch existing employees in to a map (id -> faculty)
        Map<String, Faculty> existingFaculties = facultyRepository.findAll().stream()
            .collect(Collectors.toMap(Faculty::getCode, Function.identity()));

        // Map new employees to Faculty if not already present
        List<Faculty> newFaculties = rawEmployees.stream()
            .filter(emp -> !existingFaculties.containsKey(emp.getEmpId()))
            .map(emp -> new Faculty(
                0,
                emp.getEmpId(),
                emp.getName(),
                emp.getGender(),
                FacultyRole.TEACHER, // default role
                emp.getSalutation(),
                passwordEncoder.encode(emp.getPwd()),
                null // courseAssignments is initialized as null (or empty list)
            ))
            .collect(Collectors.toList());

        if (!newFaculties.isEmpty()) {
            facultyRepository.saveAll(newFaculties);

            newFaculties.forEach(nf -> existingFaculties.put(nf.getCode(), nf));
        }

    }

}
