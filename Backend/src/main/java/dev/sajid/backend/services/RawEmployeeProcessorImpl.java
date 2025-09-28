package dev.sajid.backend.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.raw.Employee;
import dev.sajid.backend.repositories.FacultyRepository;

public class RawEmployeeProcessorImpl implements RawEmployeeProcessor {
    @Autowired
    FacultyRepository facultyRepository;

    @Override
    public Map<Integer, Faculty> processEmployees(List<Employee> rawEmployees) {
        // fetch existing employees in to a map (id -> faculty)
        Map<Integer, Faculty> existingFaculties = facultyRepository.findAll().stream()
            .collect(Collectors.toMap(Faculty::getId, Function.identity()));

        // Map new employees to Faculty if not already present
        List<Faculty> newFaculties = rawEmployees.stream()
            .filter(emp -> !existingFaculties.containsKey(emp.getEmpId()))
            .map(emp -> new Faculty(
                0,
                emp.getEmpId(),
                emp.getName(),
                emp.getGender(),
                emp.getSalutation(),
                emp.getPwd(),
                null // courseAssignments is initialized as null (or empty list)
            ))
            .collect(Collectors.toList());

        if (!newFaculties.isEmpty()) {
            facultyRepository.saveAll(newFaculties);

            newFaculties.forEach(nf -> existingFaculties.put(nf.getId(), nf));
        }

        return existingFaculties;
    }

}
