package dev.sajid.backend.dtos;

import java.util.Map;

public record BranchTree(
        int branchCode,
        String branchName,
        Map<Integer, Map<String, Integer>> semesters
){
}
