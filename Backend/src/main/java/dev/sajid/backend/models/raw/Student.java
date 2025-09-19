package dev.sajid.backend.models.raw;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {
    private String roll;
    private String name;
    private String branch;
    private String section;
    private int sem;
    private String scheme;
    private String degree;
}
