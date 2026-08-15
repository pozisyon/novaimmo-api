package com.novaimmo.demo.contact;


import com.novaimmo.demo.contact.dto.ContactResponse;
import com.novaimmo.demo.contact.dto.CreateContactRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository repository;

    public ContactService(
            ContactRepository repository
    ) {
        this.repository = repository;
    }


    public ContactResponse create(
            CreateContactRequest request
    ) {

        Contact contact = new Contact();

        contact.setNom(request.nom());
        contact.setEmail(request.email());
        contact.setTelephone(request.telephone());
        contact.setSujet(request.sujet());
        contact.setMessage(request.message());

        Contact saved =
                repository.save(contact);

        return toResponse(saved);
    }


    public List<ContactResponse> findAll() {

        return repository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public List<ContactResponse> findNewContacts() {

        return repository
                .findByStatutOrderByCreatedAtDesc(
                        "NOUVEAU"
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public ContactResponse findById(
            Long id
    ) {

        Contact contact =
                findEntity(id);

        return toResponse(contact);
    }


    public ContactResponse markAsRead(
            Long id
    ) {

        Contact contact =
                findEntity(id);

        contact.setStatut("LU");

        return toResponse(
                repository.save(contact)
        );
    }


    public ContactResponse markAsProcessed(
            Long id
    ) {

        Contact contact =
                findEntity(id);

        contact.setStatut("TRAITE");

        return toResponse(
                repository.save(contact)
        );
    }


    public void delete(
            Long id
    ) {

        Contact contact =
                findEntity(id);

        repository.delete(contact);
    }


    private Contact findEntity(
            Long id
    ) {

        return repository
                .findById(id)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Message introuvable"
                                )
                );
    }


    private ContactResponse toResponse(
            Contact contact
    ) {

        return new ContactResponse(

                contact.getId(),

                contact.getNom(),

                contact.getEmail(),

                contact.getTelephone(),

                contact.getSujet(),

                contact.getMessage(),

                contact.getStatut(),

                contact.getCreatedAt()
        );
    }
}