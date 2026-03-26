package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.*;
import edu.unimagdalena.lms.entities.Instructor;
import edu.unimagdalena.lms.entities.InstructorProfile;
import edu.unimagdalena.lms.repositories.InstructorProfileRepository;
import edu.unimagdalena.lms.repositories.InstructorRepository;
import edu.unimagdalena.lms.services.mapper.InstructorProfileMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InstructorProfileServiceImpl implements InstructorProfileService {
    private final InstructorProfileRepository repo;
    private final InstructorProfileMapper mapper;
    private final InstructorRepository instructorRepo;

    @Override
    public InstructorProfileResponse create(InstructorProfileRequestCreate dto) {
        Instructor instructor = instructorRepo.findById(dto.instructorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Instructor %d not found".formatted(dto.instructorId())
                ));

        InstructorProfile instructorProfile = mapper.toEntity(dto);
        instructorProfile.setInstructor(instructor);

        return mapper.toResponse(repo.save(instructorProfile));
    }

    @Override
    public InstructorProfileResponse update(Long id, InstructorProfileRequestUpdate dto) {
        InstructorProfile instructorProfile = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("InstructorProfile %d not found".formatted(id)
        ));

        mapper.patch(instructorProfile, dto);
        return mapper.toResponse(repo.save(instructorProfile));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse findById(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "InstructorProfile %d not found".formatted(id)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorProfileResponse> findAll() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse findByInstructorId(Long instructorId) {
        return repo.findByInstructorId(instructorId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "InstructorProfile for instructor %d not found".formatted(instructorId)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse findByPhone(String phone) {
        return repo.findByPhone(phone)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "InstructorProfile with phone %s not found".formatted(phone)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByInstructorId(Long instructorId) {
        return repo.existsByInstructorId(instructorId);
    }
}
