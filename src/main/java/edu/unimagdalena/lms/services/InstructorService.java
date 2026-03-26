package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorDtos.*;

import java.util.List;

public interface InstructorService {
    InstructorResponse create(InstructorRequestCreate dto);
    InstructorResponse get(Long id);
    Boolean existsByEmail(String email);
    List<InstructorResponse> getAll();
    List<InstructorResponse> getAllByOrderByFullNameAsc();
}
