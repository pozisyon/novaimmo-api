package com.novaimmo.demo.contact;


import com.novaimmo.demo.contact.dto.ContactResponse;
import com.novaimmo.demo.contact.dto.CreateContactRequest;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService service;

    public ContactController(
            ContactService service
    ) {
        this.service = service;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse create(

            @Valid
            @RequestBody
            CreateContactRequest request
    ) {

        return service.create(request);
    }


    @GetMapping
    public List<ContactResponse> findAll() {

        return service.findAll();
    }


    @GetMapping("/new")
    public List<ContactResponse> findNew() {

        return service.findNewContacts();
    }


    @GetMapping("/{id}")
    public ContactResponse findById(

            @PathVariable Long id
    ) {

        return service.findById(id);
    }


    @PatchMapping("/{id}/read")
    public ContactResponse markAsRead(

            @PathVariable Long id
    ) {

        return service.markAsRead(id);
    }


    @PatchMapping("/{id}/processed")
    public ContactResponse markAsProcessed(

            @PathVariable Long id
    ) {

        return service.markAsProcessed(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(

            @PathVariable Long id
    ) {

        service.delete(id);
    }
}