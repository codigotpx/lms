package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorDtos.*;
import edu.unimagdalena.lms.entities.Instructor;
import edu.unimagdalena.lms.entities.InstructorProfile;
import edu.unimagdalena.lms.repositories.InstructorProfileRepository;
import edu.unimagdalena.lms.repositories.InstructorRepository;
import edu.unimagdalena.lms.services.mapper.InstructorMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    private final InstructorMapper mapper;
    private final InstructorRepository repo;
    private final InstructorProfileRepository instructorProfileRepo;

    @Override @Transactional
    public InstructorResponse create(InstructorRequestCreate dto) {
        Instructor instructor = mapper.toEntity(dto);
        instructor.setCreatedAt(Instant.now());
        instructor.setUpdatedAt(Instant.now());
        var Entity =  repo.save(instructor);
        return mapper.toResponse(Entity);
    }

    @Override @Transactional(readOnly = true)
    public InstructorResponse get(Long id) {
        return repo.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Instructor %d not found".formatted(id)));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public List<InstructorResponse> getAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<InstructorResponse> getAllByOrderByFullNameAsc() {
        return repo.findAllByOrderByFullNameAsc().stream().map(mapper::toResponse).toList();
    }
}
