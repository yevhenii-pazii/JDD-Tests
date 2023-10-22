package com.example.jddtests.petservice.pet;

import com.example.jddtests.petservice.notification.NotificationClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {

    private final NotificationClientService notificationClientService;
    private final PetRepository petRepository;

    public Page<Pet> findAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    public Pet save(Pet entity) {
        var pet =  petRepository.save(entity);
        notificationClientService.notifyClinic(pet);
        return pet;
    }

    public Pet getReferenceById(Long id) {
        return petRepository.getReferenceById(id);
    }
}
