package dev.sajid.backend.services.keys;

public record StudentBatchKey(BranchKey bkey, int semester, String section) {}
