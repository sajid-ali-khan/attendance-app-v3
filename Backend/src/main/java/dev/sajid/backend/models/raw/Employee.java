package dev.sajid.backend.models.raw;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    private int empId;
    private String pwd;
    private String gender;
    private String salutation;
    private String name;
}
