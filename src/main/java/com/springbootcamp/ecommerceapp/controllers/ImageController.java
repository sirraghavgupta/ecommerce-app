package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.entities.ProductVariation;
import com.springbootcamp.ecommerceapp.repos.ProductVariationRepository;
import com.springbootcamp.ecommerceapp.services.ImageService;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import com.springbootcamp.ecommerceapp.utils.ErrorVO;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @Autowired
    ProductVariationRepository variationRepository;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/user/image")
    public BaseVO uploadImage(@RequestBody() MultipartFile file, HttpServletRequest request) {
        BaseVO response;
        String email = request.getUserPrincipal().getName();
        return imageService.storeUserProfileImage(file, email);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/product-variation/images/{variationId}")
    public List<BaseVO> uploadSecondaryImagesForProductVariation(@PathVariable Long variationId, @RequestBody MultipartFile[] files) {

        Optional<ProductVariation> variation = variationRepository.findByIdAndIsDeletedFalse(variationId);
        if(!variation.isPresent()){
            return Arrays.asList(new ErrorVO("Validation failed.", "Product variation does not exist", new Date()));
        }

        return Arrays.asList(files)
                .stream()
                .map(file -> imageService.uploadProductVariationImage(file, variationId))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/product-variation/image/{variationId}")
    public BaseVO uploadPrimaryImageForProductVariation(@PathVariable Long variationId, @RequestBody MultipartFile file) {

        Optional<ProductVariation> variation = variationRepository.findByIdAndIsDeletedFalse(variationId);
        if(!variation.isPresent()){
            return new ErrorVO("Validation failed.", "Product variation does not exist", new Date());
        }

        return imageService.uploadPrimaryImageForProductVariation(file, variationId);
    }


//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/downloadImage/{fileName:.+}")
//    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName, HttpServletRequest request) {
//        // Load file as Resource
//        Resource resource = imageService.loadImageAsResource(fileName);
//
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            logger.info("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
}