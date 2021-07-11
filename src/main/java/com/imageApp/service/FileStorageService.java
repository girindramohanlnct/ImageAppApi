package com.imageApp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.imageApp.config.FileStorageProperties;
import com.imageApp.exception.FileStorageException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String name = "";

        try {
        	if(!StringUtils.cleanPath(file.getContentType()).contains("image/jpeg")) {
        		throw new FileStorageException("File should be in the form of jpg jpeg or png");
        	};
        	System.out.println(StringUtils.cleanPath(file.getContentType()));
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(fileName.contains(".jpg")) {
            	name = fileName.replaceAll(".jpg", "");
            	name  = name + new Date().getTime() + ".jpg";
            }
            else if(fileName.contains(".jpeg")) {
            	name = fileName.replaceAll(".jpeg", "");
            	name  = name + new Date().getTime() + ".jpeg";
            }
            else if(fileName.contains(".png")) {
            	name = fileName.replaceAll(".png", "");
            	name  = name + new Date().getTime() + ".png";
            }
            else {
            	throw new FileStorageException("File should be in the form of jpg jpeg or png");
            }
            System.out.println(name);
            Path targetLocation = this.fileStorageLocation.resolve(name);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return name;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
    	Resource resource = null;
    	try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
             resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (Exception ex) {
//            throw new Exception("File not found " + fileName, ex);
        }
        return resource;
    }
}