package com.imageApp.controller;


import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.imageApp.model.Image;
import com.imageApp.repo.ImageRepo;
import com.imageApp.response.Response;
import com.imageApp.service.FileStorageService;

@RestController
public class ImageController {
	
	@Autowired
	public ImageRepo repo;
	@Autowired
    private FileStorageService fileStorageService;
	
	@GetMapping("/show/{id}")
	public Response getImage(@PathVariable(value = "id", required = true) Integer id) {
		System.out.println("INNNNNNNNNNNNNN "+ id);
		Image img = (Image)repo.findById(id).orElse(new Image());
		System.out.println(img);
		Response res = null;
		if(null != img) {
			res 	=  new Response();
			if(null != res) {
				res.setStatus(true);
				List<Image> l =  new ArrayList<>();
				l.add(img);
				res.setData(l);
			}
		}
		return res;
	}
	
	@GetMapping("/show")
	public Response getImages() {
		List<Image> img = repo.findAll();
		Response res =  new Response();
		if(null == res) {
			res.setStatus(false);
			res.setData(null);
		} else {
			res.setStatus(true);
			List<Image> l =  new ArrayList<>();
			l.addAll(img);
			res.setData(l);
		}
		return res;
	}
	

	@PostMapping("/")
	public Image saveImage(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
			@RequestParam("details") String details, HttpServletRequest request) {
		Image img = new Image();
		String fileName = fileStorageService.storeFile(file);
		img.setName(name);
		img.setDetails(details);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName)
                .toUriString();
		System.out.println(fileDownloadUri);
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() ;
		System.out.println("GGGGGGGGGGGGGGGG " + url);
		String urlImage = url + "/images/"+ fileName;
		img.setUrl(fileDownloadUri);
		Image result  =  repo.save(img);
		if (result.equals(null)) {
			return null;
		}
		return result;
	}
	
	@GetMapping("/images/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
