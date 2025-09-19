package dev.sajid.backend.models.raw;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {
    private String degree;
    private String scheme;
    private String branch;
    private int sem;
    private String sCode;
    private String subName;
}
