package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.course.Degree;

public record ProgramKey(Degree degree, int schemeId, int branchId) {}