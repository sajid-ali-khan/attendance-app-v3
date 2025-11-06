package dev.sajid.backend.dtos;

public record BranchDto(
        int branchCode,
        String shortForm,
        String fullForm
) {
}
