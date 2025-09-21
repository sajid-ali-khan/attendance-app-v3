package dev.sajid.backend;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.models.normalized.course.Degree;
import dev.sajid.backend.models.normalized.course.Program;
import dev.sajid.backend.models.normalized.course.Scheme;
import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.repositories.BranchRepository;
import dev.sajid.backend.repositories.ProgramRepository;
import dev.sajid.backend.repositories.SchemeRepository;
import dev.sajid.backend.services.RawCourseProcessor;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    SchemeRepository schemeRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    RawCourseProcessor rawCourseProcessor;

    @BeforeEach
    void setUpSchemesAndBranches(){
        Scheme scheme1 = new Scheme();
        scheme1.setCode("20");
        Scheme scheme2 = new Scheme();
        scheme2.setCode("23");

        schemeRepository.save(scheme1);
        schemeRepository.save(scheme2);

        Branch branch1 = new Branch();
        branch1.setBranchCode(1);
        branch1.setShortForm("CSE");

        Branch branch2 = new Branch();
        branch2.setBranchCode(2);
        branch2.setShortForm("CST");

        branchRepository.save(branch1);
        branchRepository.save(branch2);

        Program program = new Program();
        program.setDegree(Degree.BTech);
        program.setBranch(branch2); // branch code = 2
        program.setScheme(scheme1); // scheme = 20

        Program program1= new Program();
        program1.setDegree(Degree.BTech);
        program1.setBranch(branch1); // branch code = 1
        program1.setScheme(scheme1); // scheme = 20

        programRepository.save(program);
        programRepository.save(program1);
    }

    @Test
    void checkFindIdByCode(){
        int id20 = schemeRepository.findIdByCode("20");
        System.out.println("Scheme.Id for 20 is " + id20);
        int id23 = schemeRepository.findIdByCode("23");
        System.out.println("Scheme.Id for 23 is " + id23);
    }

    @Test
    void testProgramExistsBySchemeAndBranch(){
        boolean programExists = programRepository.existsBySchemeAndBranch("20", 2);
        Assertions.assertThat(programExists).isEqualTo(true);
    }

    @Test
    void testExtractSchemes(){
        List<Course> rawCourses = new ArrayList<>();
        rawCourses.add(new Course("BTECH", "20", "11", 4, "EM1", "Engineering Mathematics 1"));
        rawCourses.add(new Course("BTECH", "23", "33", 4, "DAA", "Design and Analysis of Algorithms"));
        rawCourses.add(new Course("BTECH", "20", "23", 4, "MEFA", "Managerial Economics and Financial Economics"));
        rawCourseProcessor.findOrCreateSchemes(rawCourses);

        List<Scheme> schemes = schemeRepository.findAll();

        for (Scheme scheme: schemes){
            System.out.println(scheme);
        }
    }

}
