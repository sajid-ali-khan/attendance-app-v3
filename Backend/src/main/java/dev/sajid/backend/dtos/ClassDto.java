package dev.sajid.backend.dtos;

// class - subject + branch + sem + section
public record ClassDto(
        String subjectName,
        String branchName,
        Integer semester,
        String section
) {

}
