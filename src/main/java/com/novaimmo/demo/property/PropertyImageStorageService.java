package com.novaimmo.demo.property;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PropertyImageStorageService {

    private final Path rootDirectory =
            Paths.get("uploads/properties")
                    .toAbsolutePath()
                    .normalize();


    public PropertyImageStorageService()
            throws IOException {

        Files.createDirectories(
                rootDirectory
        );
    }


    public String store(
            Long propertyId,
            MultipartFile file
    ) {

        if (
                file == null
                        ||
                        file.isEmpty()
        ) {

            throw new RuntimeException(
                    "Le fichier image est obligatoire"
            );
        }


        String contentType =
                file.getContentType();


        if (
                contentType == null
                        ||
                        !contentType.startsWith("image/")
        ) {

            throw new RuntimeException(
                    "Le fichier doit être une image"
            );
        }


        String originalFilename =
                file.getOriginalFilename();


        String extension =
                getExtension(
                        originalFilename
                );


        String filename =
                UUID.randomUUID()
                        + extension;


        Path propertyDirectory =
                rootDirectory
                        .resolve(
                                propertyId.toString()
                        )
                        .normalize();


        try {

            Files.createDirectories(
                    propertyDirectory
            );


            Path destination =
                    propertyDirectory
                            .resolve(filename)
                            .normalize();


            if (
                    !destination.startsWith(
                            propertyDirectory
                    )
            ) {

                throw new RuntimeException(
                        "Chemin de fichier invalide"
                );
            }


            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );


            return "/uploads/properties/"
                    + propertyId
                    + "/"
                    + filename;

        } catch (IOException exception) {

            throw new RuntimeException(
                    "Impossible d'enregistrer l'image",
                    exception
            );
        }
    }


    private String getExtension(
            String filename
    ) {

        if (
                filename == null
                        ||
                        !filename.contains(".")
        ) {

            return "";
        }


        return filename.substring(
                filename.lastIndexOf(".")
        );
    }
}