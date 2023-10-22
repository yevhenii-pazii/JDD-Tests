package com.example.jddtests.petservice.pet;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public Page<PetResponse> getPets(Pageable pageable) {
        return petService.findAll(pageable)
                .map(p -> PetResponse.builder().id(p.getId().toString()).name(p.getName()).build());
    }

    @GetMapping("/{id}")
    public PetResponse updatePet(@PathVariable Long id) {
        var pet = petService.getReferenceById(id);
        return PetResponse.builder().id(pet.getId().toString()).name(pet.getName()).build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse createPet(@RequestBody PetRequest request) {
        var pet = petService.save(Pet.builder().name(request.getName()).build());
        return PetResponse.builder().id(pet.getId().toString()).name(pet.getName()).build();
    }

    @PutMapping("/{id}")
    public PetResponse updatePet(@RequestBody PetRequest request, @PathVariable Long id) {
        var pet = petService.save(Pet.builder().id(id).name(request.getName()).build());
        return PetResponse.builder().id(pet.getId().toString()).name(pet.getName()).build();
    }
}
