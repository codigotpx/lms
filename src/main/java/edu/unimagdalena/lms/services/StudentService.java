package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.StudentDtos.*;

import java.util.List;

public interface StudentService {
    StudentResponse create(StudentCreate req);
    StudentResponse getById(Long id);
    List<StudentResponse> getAll();
    StudentResponse update(Long id, StudentUpdate dto);
    void delete(Long id);
}
