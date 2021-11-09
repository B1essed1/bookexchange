package com.example.bookexchange.data.service.reference;

import com.example.bookexchange.data.model.reference.Reference;
import com.example.bookexchange.data.repository.reference.ReferenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ReferenceService {

    @Autowired
    private ReferenceRepository referenceRepository;

    public Reference findByCode(String code) {
        Optional<Reference> reference = referenceRepository.findByCode(code.toLowerCase());
        if (reference.isPresent()) {
            return reference.get();
        } else {
            return null;
        }
    }

    public Reference parentByCode(String code) {
        Optional<Reference> reference = referenceRepository.findParentByCode(code.toLowerCase());
        if (reference.isPresent()) {
            return reference.get();
        } else {
            return null;
        }
    }

    public void saveReference(Reference reference) {
        referenceRepository.save(reference);
        log.info("In save - reference: {} successfully saved", reference);
    }
}
