package dev.sajid.backend.services;

import java.util.Map;
import java.util.List;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.raw.Employee;

public interface RawEmployeeProcessor {
    Map<Integer, Faculty> processEmployees(List<Employee> rawEmployees);
}
