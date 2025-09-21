package dev.sajid.backend.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.models.normalized.course.Degree;
import dev.sajid.backend.models.normalized.course.Program;
import dev.sajid.backend.models.normalized.course.ProgramSubject;
import dev.sajid.backend.models.normalized.course.Scheme;
import dev.sajid.backend.models.normalized.course.Subject;
import dev.sajid.backend.models.normalized.course.SubjectType;
import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.repositories.BranchRepository;
import dev.sajid.backend.repositories.ProgramRepository;
import dev.sajid.backend.repositories.ProgramSubjectRepository;
import dev.sajid.backend.repositories.SchemeRepository;
import dev.sajid.backend.repositories.SubjectRepository;
import jakarta.transaction.Transactional;

@Component
public class RawCourseProcessorImpl implements RawCourseProcessor {

    private final ProgramSubjectRepository programSubjectRepository;

    private final SubjectRepository subjectRepository;

    private final SchemeRepository schemeRepository;

    private final BranchRepository branchRepository;

    private final ProgramRepository programRepository;

    RawCourseProcessorImpl(ProgramRepository programRepository, BranchRepository branchRepository,
            SchemeRepository schemeRepository, SubjectRepository subjectRepository, ProgramSubjectRepository programSubjectRepository) {
        this.programRepository = programRepository;
        this.branchRepository = branchRepository;
        this.schemeRepository = schemeRepository;
        this.subjectRepository = subjectRepository;
        this.programSubjectRepository = programSubjectRepository;
    }

    // group raw courses on (degree, scheme, branchcode)
    record ProgramKey(String degree, String scheme, Character branch) {}

    // group rawCourses by (sCode, subName)
    record SubjectKey(String shortForm, String fullForm) {}

    // group rawCourses by (programKey, subjectKey)
    record ProgramSubjectKey(ProgramKey programKey, SubjectKey subjectKey, int semester){}

    @Override
    @Transactional
    public boolean processRawCourse(List<Course> rawCourses) {
        return extractSchemes(rawCourses) &&
        extractBranches(rawCourses) &&
        extractPrograms(rawCourses) &&
        extractSubjects(rawCourses) &&
        formProgramSubjects(rawCourses);
    }

    @Override
    public boolean extractSchemes(List<Course> rawCourses) {
        Set<String> schemes = new HashSet<>();

        for (Course course : rawCourses) {
            schemes.add(course.getScheme());
        }

        Iterator<String> it = schemes.iterator();

        try {
            while (it.hasNext()) {
                String scheme = it.next();
                Scheme sch = new Scheme();
                sch.setCode(scheme);
                schemeRepository.save(sch);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }

        return true;
    }

    @Override
    public boolean extractBranches(List<Course> rawCourses) {
        // create a set of branch code(first digit of the branch from raw courses)
        Set<Character> branchCodes = new HashSet<>();

        for (Course course : rawCourses) {
            branchCodes.add(course.getBranch().toCharArray()[0]);
        }

        Iterator<Character> it = branchCodes.iterator();

        try {
            while (it.hasNext()) {
                int branchCode = Integer.parseInt(it.next().toString());
                Branch b = new Branch();
                b.setBranchCode(branchCode);
                b.setShortForm(BranchNamer.getBranchShortNameByCode(branchCode));
                b.setFullForm(BranchNamer.getBranchFullNameByCode(branchCode));

                branchRepository.save(b);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        return true;

    }

    @Override
    public boolean extractPrograms(List<Course> rawCourses) {

        List<ProgramKey> programs = rawCourses.stream()
                .map(c -> new ProgramKey(c.getDegree(), c.getScheme(), c.getBranch().charAt(0)))
                .distinct()
                .toList();

        try {
            for (ProgramKey p : programs) {
                Program program = new Program();
                // setting degree
                program.setDegree(p.degree.equalsIgnoreCase("btech") ? Degree.BTech : Degree.MTech);

                // setting scheme
                Scheme scheme = schemeRepository.findByCode(p.scheme).get();
                program.setScheme(scheme);

                // setting branch
                int branchCode = Integer.parseInt(p.branch.toString());
                Branch branch = branchRepository.findByBranchCode(branchCode).get();
                program.setBranch(branch);

                programRepository.save(program);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean extractSubjects(List<Course> rawCourses) {

        List<SubjectKey> subjects = rawCourses.stream()
                .map(c -> new SubjectKey(c.getSCode(), c.getSubName()))
                .distinct()
                .toList();

        try {
            for (SubjectKey sub : subjects) {
                Subject subject = new Subject();
                subject.setShortForm(sub.shortForm);
                subject.setFullForm(sub.fullForm);
                subject.setSubjectType(sub.shortForm.endsWith("(P)") ? SubjectType.Lab : SubjectType.Theory);

                subjectRepository.save(subject);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean formProgramSubjects(List<Course> rawCourses) {
        List<ProgramSubjectKey> programSubjectKeys = rawCourses
        .stream()
        .map(c -> new ProgramSubjectKey(
            new ProgramKey(c.getDegree(), c.getScheme(), c.getBranch().charAt(0)), 
            new SubjectKey(c.getSCode(), c.getSubName()), 
            c.getSem()))
        .distinct()
        .toList();
        
        try {
            for (ProgramSubjectKey pskey: programSubjectKeys){
                Program program = new Program();
                
                Scheme scheme = new Scheme();
                scheme.setCode(pskey.programKey.scheme);

                Branch branch = new Branch();
                int branchCode = Integer.parseInt(pskey.programKey.branch.toString());
                branch.setBranchCode(branchCode);
                branch.setShortForm(BranchNamer.getBranchShortNameByCode(branchCode));
                branch.setShortForm(BranchNamer.getBranchFullNameByCode(branchCode));

                program.setDegree(pskey.programKey.degree.equalsIgnoreCase("btech")? Degree.BTech: Degree.MTech);
                program.setScheme(null);
                program.setBranch(branch);

                Subject subject = new Subject();
                subject.setShortForm(pskey.subjectKey.shortForm);
                subject.setFullForm(pskey.subjectKey.fullForm);
                subject.setSubjectType(pskey.subjectKey.shortForm.endsWith("(P)") ? SubjectType.Lab: SubjectType.Theory);

                ProgramSubject programSubject = new ProgramSubject();
                programSubject.setProgram(program);
                programSubject.setSubject(subject);
                programSubject.setSemester(pskey.semester);

                programSubjectRepository.save(programSubject);
            }
        }catch(Exception ex){
            System.out.println(ex);
            return false;
        }
        return true;
    }

}
