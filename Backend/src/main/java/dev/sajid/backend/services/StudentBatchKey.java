package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.course.Degree;

public record StudentBatchKey(Degree degree, String schemeCode, int branchCode, int semester, String section) {}
