package dev.sajid.backend.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.sajid.backend.models.normalized.course.Scheme;
import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.repositories.SchemeRepository;

@Component
public class RawCourseProcessorImpl implements RawCourseProcessor {

    @Autowired
    SchemeRepository schemeRepository;

    @Override
    public boolean processRawCourse(List<Course> rawCourses) {
        return extractSchemes(rawCourses);
    }

    @Override
    public boolean extractSchemes(List<Course> rawCourses) {
        Set<String> schemes = new HashSet<>();

        for (Course course: rawCourses){
            schemes.add(course.getScheme());
        }

        Iterator<String> it = schemes.iterator();

        try{
            while (it.hasNext()){
            String scheme = it.next();
            Scheme sch = new Scheme();
            sch.setCode(scheme);
            schemeRepository.save(sch);
        }
        }catch(Exception ex){
            System.out.println(ex);
            return false;
        }

        return true;
    }

}
