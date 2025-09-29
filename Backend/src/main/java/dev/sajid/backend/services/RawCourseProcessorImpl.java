package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.course.*;
import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RawCourseProcessorImpl implements RawCourseProcessor {

    private final ProgramSubjectRepository programSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final SchemeRepository schemeRepository;
    private final BranchRepository branchRepository;
    private final ProgramRepository programRepository;

    public RawCourseProcessorImpl(ProgramRepository programRepository, BranchRepository branchRepository,
                                  SchemeRepository schemeRepository, SubjectRepository subjectRepository,
                                  ProgramSubjectRepository programSubjectRepository) {
        this.programRepository = programRepository;
        this.branchRepository = branchRepository;
        this.schemeRepository = schemeRepository;
        this.subjectRepository = subjectRepository;
        this.programSubjectRepository = programSubjectRepository;
    }

    @Override
    @Transactional
    public void processRawCourses(List<Course> rawCourses) {
        Map<String, Scheme> schemesMap = findOrCreateSchemes(rawCourses);
        Map<Integer, Branch> branchesMap = findOrCreateBranches(rawCourses);
        Map<SubjectKey, Subject> subjectsMap = findOrCreateSubjects(rawCourses);
        Map<ProgramKey, Program> programsMap = findOrCreatePrograms(rawCourses, schemesMap, branchesMap);

        createProgramSubjects(rawCourses, schemesMap, branchesMap, programsMap, subjectsMap);
    }

    @Override
    public Map<String, Scheme> findOrCreateSchemes(List<Course> rawCourses) {
        Map<String, Scheme> existingSchemes = schemeRepository.findAll().stream()
                .collect(Collectors.toMap(s -> s.getCode().trim().toUpperCase(), Function.identity()));

        List<Scheme> newSchemes = rawCourses.stream()
                .map(Course::getScheme)
                .map(s -> s.trim().toUpperCase())
                .distinct()
                .filter(schemeCode -> !existingSchemes.containsKey(schemeCode))
                .map(schemeCode -> new Scheme(0, schemeCode, 0, null))
                .toList();

        if (!newSchemes.isEmpty()) {
            schemeRepository.saveAll(newSchemes);
            newSchemes.forEach(s -> existingSchemes.put(s.getCode().trim().toUpperCase(), s));
        }

        return existingSchemes;
    }

    public Map<Integer, Branch> findOrCreateBranches(List<Course> rawCourses) {
        Map<Integer, Branch> existingBranches = branchRepository.findAll().stream()
                .collect(Collectors.toMap(Branch::getBranchCode, Function.identity()));

        List<Branch> newBranches = rawCourses.stream()
                .map(c -> Integer.parseInt(c.getBranch().substring(0, 1)))
                .distinct()
                .filter(branchCode -> !existingBranches.containsKey(branchCode))
                .map(branchCode -> new Branch(0, branchCode,
                        BranchNamer.getBranchShortNameByCode(branchCode),
                        BranchNamer.getBranchFullNameByCode(branchCode)))
                .toList();

        if (!newBranches.isEmpty()) {
            branchRepository.saveAll(newBranches);
            newBranches.forEach(b -> existingBranches.put(b.getBranchCode(), b));
        }

        return existingBranches;
    }

    public Map<SubjectKey, Subject> findOrCreateSubjects(List<Course> rawCourses) {
        Map<SubjectKey, Subject> existingSubjects = subjectRepository.findAll().stream()
                .collect(Collectors.toMap(s -> new SubjectKey(s.getShortForm().trim().toUpperCase(), s.getFullForm().trim().toUpperCase()), Function.identity()));

        List<Subject> newSubjects = rawCourses.stream()
                .map(rs -> {
                    return new SubjectKey(rs.getSCode().trim().toUpperCase(), rs.getSubName().trim().toUpperCase());
                })
                .distinct()
                .filter(key -> !existingSubjects.containsKey(key))
                .map(key -> {
                    Subject s = new Subject();
                    s.setShortForm(key.shortForm());
                    s.setFullForm(key.fullForm());
                    s.setSubjectType(key.shortForm().endsWith("(P)") ? SubjectType.Lab : SubjectType.Theory);
                    return s;
                })
                .toList();

        if (!newSubjects.isEmpty()) {
            subjectRepository.saveAll(newSubjects);
            newSubjects.forEach(s -> {
                SubjectKey key = new SubjectKey(s.getShortForm().trim().toUpperCase(), s.getFullForm().trim().toUpperCase());
                existingSubjects.put(key, s);
            });
        }

        return existingSubjects;
    }

    @Override
    public Map<ProgramKey, Program> findOrCreatePrograms(List<Course> rawCourses, Map<String, Scheme> schemesMap,
                                                         Map<Integer, Branch> branchesMap) {
        Map<ProgramKey, Program> existingPrograms = programRepository.findAll().stream()
                .collect(Collectors.toMap(
                        p -> new ProgramKey(p.getDegree(), p.getScheme().getId(), p.getBranch().getId()),
                        Function.identity()
                ));

        List<Program> newPrograms = rawCourses.stream()
                .map(c -> {
                    Degree degree = c.getDegree().equalsIgnoreCase("btech") ? Degree.BTech : Degree.MTech;
                    String schemeCode = c.getScheme().trim().toUpperCase();
                    Scheme scheme = schemesMap.get(schemeCode);
                    int branchId = Integer.parseInt(c.getBranch().substring(0, 1));
                    Branch branch = branchesMap.get(branchId);
                    return new ProgramKey(degree, scheme.getId(), branch.getId());
                })
                .distinct()
                .filter(key -> !existingPrograms.containsKey(key))
                .map(key -> {
                    Program p = new Program();
                    p.setDegree(key.degree());
                    p.setScheme(schemesMap.values().stream().filter(s -> s.getId() == key.schemeId()).findFirst().get());
                    p.setBranch(branchesMap.get(key.branchId()));
                    return p;
                })
                .toList();

        if (!newPrograms.isEmpty()) {
            programRepository.saveAll(newPrograms);
            newPrograms.forEach(p -> existingPrograms.put(new ProgramKey(p.getDegree(), p.getScheme().getId(), p.getBranch().getId()), p));
        }

        return existingPrograms;
    }

    @Override
    public void createProgramSubjects(List<Course> rawCourses, Map<String, Scheme> schemesMap,
                                      Map<Integer, Branch> branchesMap, Map<ProgramKey, Program> programsMap,
                                      Map<SubjectKey, Subject> subjectsMap) {
        Set<ProgramSubjectKey> existingLinks = programSubjectRepository.findAll().stream()
                .map(ps -> new ProgramSubjectKey(ps.getProgram().getId(), ps.getSubject().getId(), ps.getSemester()))
                .collect(Collectors.toSet());

        List<ProgramSubject> newLinks = rawCourses.stream()
                .map(c -> {
                    Degree degree = c.getDegree().equalsIgnoreCase("btech") ? Degree.BTech : Degree.MTech;
                    String schemeCode = c.getScheme().trim().toUpperCase();
                    int branchId = Integer.parseInt(c.getBranch().substring(0, 1));

                    Scheme scheme = schemesMap.get(schemeCode);
                    Branch branch = branchesMap.get(branchId);
                    Program program = programsMap.get(new ProgramKey(degree, scheme.getId(), branch.getId()));
                    SubjectKey subjectKey = new SubjectKey(c.getSCode().trim().toUpperCase(), c.getSubName().trim().toUpperCase());
                    Subject subject = subjectsMap.get(subjectKey);

                    if (program == null || subject == null) return null;

                    ProgramSubjectKey key = new ProgramSubjectKey(program.getId(), subject.getId(), c.getSem());
                    if (existingLinks.contains(key)) return null;

                    ProgramSubject ps = new ProgramSubject();
                    ps.setProgram(program);
                    ps.setSubject(subject);
                    ps.setSemester(c.getSem());
                    return ps;
                })
                .filter(ps -> ps != null)
                .distinct()
                .toList();

        if (!newLinks.isEmpty()) {
            programSubjectRepository.saveAll(newLinks);
        }
    }
}
