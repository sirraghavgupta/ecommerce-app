package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.config.ImageStorageProperties;
import com.springbootcamp.ecommerceapp.entities.ProductVariation;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.exception.FileStorageException;
import com.springbootcamp.ecommerceapp.exception.MyFileNotFoundException;
import com.springbootcamp.ecommerceapp.repos.ProductVariationRepository;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class ImageService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductVariationRepository variationRepository;

    @Autowired
    ImageStorageProperties fileStorageProperties;

    private Set<java.lang.String> validFileFormats = new HashSet<>();

    {
        validFileFormats.add("jpg");
        validFileFormats.add("jpeg");
        validFileFormats.add("png");
        validFileFormats.add("bmp");
    }

    private final Path imageStorageLocation;

    @Autowired
    public ImageService(ImageStorageProperties fileStorageProperties) {
        this.imageStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.imageStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public BaseVO storeUserProfileImage(MultipartFile file, String email) {
        BaseVO response;

        // get file name and extension
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension;
        Optional<String> ext = Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));

        // validate file
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(!ext.isPresent()){
                throw new FileStorageException("Sorry! File format not allowed. " + fileName);
            }
            extension = ext.get();
            if(!validFileFormats.contains(extension)) {
                throw new FileStorageException("Sorry! File format not allowed. " + fileName);
            }

            // generate the path and file name and then save the file.
            User user = userService.getUserByEmail(email);
            Long userId = user.getId();

            String newFilename =  "users/" + userId + "." + extension;
            Path targetLocation = this.imageStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadImage/")
                    .path(targetLocation.toString())
                    .toUriString();

            user.setImage(fileDownloadUri);
            userRepository.save(user);

            response = new ResponseVO<String>(fileDownloadUri, "Image uploaded successfully", new Date());


        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

        return response;
    }

    public Resource loadImageAsResource(String fileName) {
        try {
            Path filePath = this.imageStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public BaseVO uploadProductVariationImage(MultipartFile file, Long id) {
        BaseVO response;

        // get file name and extension
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension;
        Optional<String> ext = Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));

        // validate file
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(!ext.isPresent()){
                throw new FileStorageException("Sorry! File format not allowed. " + fileName);
            }
            extension = ext.get();
            if(!validFileFormats.contains(extension)) {
                throw new FileStorageException("Sorry! File format not allowed. " + fileName);
            }

            // generate path for the image
            ProductVariation variation = variationRepository.findById(id).get();

            String directoryPath = fileStorageProperties.getUploadDir() + "/products/" +
                                    variation.getProduct().getId() + "/variations/" + variation.getId();
            File parentDir = new File(directoryPath);
            parentDir.mkdirs();

            Integer imageId = getAllFilesFromFolder(directoryPath).size() + 1;

//            BigDecimal imageId = variationRepository.getNextValMySequence();
//            System.out.println(imageId + "#####################################################");
            String newFilename = "products/" + variation.getProduct().getId() +
                                "/variations/" + variation.getId() + "/" + imageId + "." + extension;

            Path targetLocation = this.imageStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String filePath = targetLocation.toString();
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadImage/")
                    .path(filePath)
                    .toUriString();

            response = new ResponseVO<String>(fileDownloadUri, "Image uploaded successfully", new Date());

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
        return response;
    }

    public List<String> getAllFilesFromFolder(String path){
        List<String> result = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(path))) {

            result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            result.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public BaseVO uploadPrimaryImageForProductVariation(MultipartFile file, Long id) {
        BaseVO response;

        // get file name and extension
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension;
        Optional<String> ext = Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));

        // validate file
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            if(!ext.isPresent()){
                throw new FileStorageException("Sorry! File format not allowed. " + fileName);
            }
            extension = ext.get();
            if(!validFileFormats.contains(extension)) {
                throw new FileStorageException("Sorry! File format not allowed. " + fileName);
            }

            // generate path for the image
            ProductVariation variation = variationRepository.findById(id).get();

            String directoryPath = fileStorageProperties.getUploadDir() + "/products/" +
                    variation.getProduct().getId() + "/variations/" + variation.getId();
            File parentDir = new File(directoryPath);
            parentDir.mkdirs();

            String newFilename = "products/" + variation.getProduct().getId() +
                    "/variations/" + variation.getId() + "/pi" + "." + extension;

            Path targetLocation = this.imageStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String filePath = targetLocation.toString();
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadImage/")
                    .path(filePath)
                    .toUriString();

            response = new ResponseVO<String>(fileDownloadUri, "Image uploaded successfully", new Date());

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
        return response;
    }
}