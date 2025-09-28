package dev.sajid.backend.models.raw;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Course {
    @CsvBindByName(column = "degr", required = true)
    private String degree;

    @CsvBindByName(column = "scheme", required = true)
    private String scheme;

    @CsvBindByName(column = "branch", required = true)
    private String branch;

    @CsvBindByName(column = "sem", required = true)
    private int sem;

    @CsvBindByName(column = "scode", required = true)
    private String sCode;

    @CsvBindByName(column = "subname", required = true)
    private String subName;
}
