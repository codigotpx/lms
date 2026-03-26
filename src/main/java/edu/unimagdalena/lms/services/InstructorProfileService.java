package edu.unimagdalena.lms.services;


import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.*;

import java.util.List;

public interface InstructorProfileService {
    InstructorProfileResponse create(InstructorProfileRequestCreate dto);
    InstructorProfileResponse update(Long id, InstructorProfileRequestUpdate dto);
    void delete(Long id);

    InstructorProfileResponse findById(Long id);
    List<InstructorProfileResponse> findAll();
    InstructorProfileResponse findByInstructorId(Long instructorId);
    InstructorProfileResponse findByPhone(String instructorName);
    Boolean existsByInstructorId(Long instructorId);

}
