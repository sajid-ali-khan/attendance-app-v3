package dev.sajid.backend.services.csv;

import dev.sajid.backend.models.normalized.course.*;
import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.repositories.*;
import dev.sajid.backend.services.keys.BranchKey;
import dev.sajid.backend.services.BranchNamer;
import dev.sajid.backend.services.keys.BranchSubjectKey;
import dev.sajid.backend.services.keys.SubjectKey;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RawCourseProcessor {

    private final SubjectRepository subjectRepository;
    private final SchemeRepository schemeRepository;
    private final BranchRepository branchRepository;
    private final BranchSubjectRepository branchSubjectRepository;

    public RawCourseProcessor(BranchRepository branchRepository,
                              SchemeRepository schemeRepository, SubjectRepository subjectRepository, BranchSubjectRepository branchSubjectRepository) {
        this.branchRepository = branchRepository;
        this.schemeRepository = schemeRepository;
        this.subjectRepository = subjectRepository;
        this.branchSubjectRepository = branchSubjectRepository;
    }

    @Transactional
    public void processRawCourses(List<Course> rawCourses) {
        Map<String, Scheme> schemesMap = findOrCreateSchemes(rawCourses);
        Map<BranchKey, Branch> branchesMap = findOrCreateBranches(rawCourses, schemesMap);
        Map<SubjectKey, Subject> subjectsMap = findOrCreateSubjects(rawCourses);

        createBranchSubjects(rawCourses, branchesMap, subjectsMap);
    }

    public Map<String, Scheme> findOrCreateSchemes(List<Course> rawCourses) {
        Map<String, Scheme> existingSchemes = schemeRepository.findAll().stream()
                .collect(Collectors.toMap(s -> s.getCode().trim().toUpperCase(), Function.identity()));

        List<Scheme> newSchemes = rawCourses.stream()
                .map(Course::getScheme)
                .map(s -> s.trim().toUpperCase())
                .distinct()
                .filter(schemeCode -> !existingSchemes.containsKey(schemeCode))
                .map(schemeCode -> new Scheme(0, schemeCode, null))
                .toList();

        if (!newSchemes.isEmpty()) {
            schemeRepository.saveAll(newSchemes);
            newSchemes.forEach(s -> existingSchemes.put(s.getCode().trim().toUpperCase(), s));
        }

        return existingSchemes;
    }

    public Map<BranchKey, Branch> findOrCreateBranches(List<Course> rawCourses, Map<String, Scheme> schemesMap) {
        Map<BranchKey, Branch> existingBranches = branchRepository.findAll()
                .stream()
                .collect(Collectors.toMap(b -> new BranchKey(b.getScheme().getCode(), b.getBranchCode()),
                        Function.identity()));

        List<Branch> newBranches = rawCourses.stream()
                .map(c -> {
                    int branchCode = Integer.parseInt(c.getBranch().substring(0, 1));
                    String schemeCode = c.getScheme().trim().toUpperCase();
                    return new BranchKey(schemeCode, branchCode);
                })
                .distinct()
                .filter(key -> !existingBranches.containsKey(key))
                .map(key -> new Branch(0, key.branchCode(),
                        BranchNamer.getBranchShortNameByCode(key.branchCode()),
                        BranchNamer.getBranchFullNameByCode(key.branchCode()),
                        schemesMap.get(key.schemeCode()),
                        null,
                        null))
                .toList();

        if (!newBranches.isEmpty()) {
            branchRepository.saveAll(newBranches);
            newBranches
                    .forEach(b -> existingBranches.put(
                            new BranchKey(b.getScheme().getCode(), b.getBranchCode()), b));
        }

        return existingBranches;
    }

    public Map<SubjectKey, Subject> findOrCreateSubjects(List<Course> rawCourses) {
        Map<SubjectKey, Subject> existingSubjects = subjectRepository.findAll().stream()
                .collect(Collectors.toMap(s -> new SubjectKey(s.getShortForm().trim().toUpperCase(),
                        s.getFullForm().trim().toUpperCase()), Function.identity()));

        List<Subject> newSubjects = rawCourses.stream()
                .map(rs -> new SubjectKey(rs.getSCode().trim().toUpperCase(), rs.getSubName().trim().toUpperCase()))
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
                SubjectKey key = new SubjectKey(s.getShortForm().trim().toUpperCase(),
                        s.getFullForm().trim().toUpperCase());
                existingSubjects.put(key, s);
            });
        }

        return existingSubjects;
    }

    public void createBranchSubjects(List<Course> rawCourses,
                                     Map<BranchKey, Branch> branchesMap, Map<SubjectKey, Subject> subjectsMap) {

        Set<BranchSubjectKey> existingBranchSubjects = branchSubjectRepository.findAll()
                .stream()
                .map(bs -> {
                    BranchKey bkey = new BranchKey(bs.getBranch().getScheme().getCode(), bs.getBranch().getBranchCode());
                    SubjectKey skey = new SubjectKey(bs.getSubject().getShortForm(), bs.getSubject().getFullForm());
                    return new BranchSubjectKey(bkey, skey, bs.getSemester());
                })
                .collect(Collectors.toSet());

        List<BranchSubject> newLinks = rawCourses.stream()
                .map(c -> {
                    String schemeCode = c.getScheme().trim().toUpperCase();
                    int branchCode = Integer.parseInt(c.getBranch().substring(0, 1));
                    BranchKey branchKey = new BranchKey(schemeCode, branchCode);
                    SubjectKey subjectKey = new SubjectKey(c.getSCode().trim().toUpperCase(),
                            c.getSubName().trim().toUpperCase());

                    Branch branch = branchesMap.get(branchKey);
                    Subject subject = subjectsMap.get(subjectKey);
                    if (branch == null || subject == null) {
                        return null;
                    }

                    BranchSubjectKey key = new BranchSubjectKey(branchKey, subjectKey, c.getSem());
                    if (existingBranchSubjects.contains(key))
                        return null;

                    BranchSubject bs = new BranchSubject();
                    bs.setBranch(branch);
                    bs.setSubject(subject);
                    bs.setSemester(c.getSem());
                    return bs;
                })
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (!newLinks.isEmpty()) {
            branchSubjectRepository.saveAll(newLinks);
        }
    }
}
