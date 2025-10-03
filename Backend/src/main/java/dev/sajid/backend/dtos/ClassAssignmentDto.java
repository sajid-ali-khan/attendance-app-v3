package dev.sajid.backend.dtos;

public record ClassAssignmentDto(
        Integer branchId,
        Integer semester,
        String section,
        Integer subjectId,
        Integer facultyId) {
}
