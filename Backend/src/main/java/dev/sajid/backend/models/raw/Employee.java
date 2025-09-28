package dev.sajid.backend.models.raw;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    @CsvBindByName(column = "empid", required = true)
    private int empId;

    @CsvBindByName(column = "pwd", required = true)
    private String pwd;

    @CsvBindByName(column = "gender", required = true)
    private String gender;
    
    @CsvBindByName(column = "salu", required = false)
    private String salutation;

    @CsvBindByName(column = "name", required = false)
    private String name;
}
