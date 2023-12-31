package com.example.socialnetworkingapp.filesystem;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:8444")
@AllArgsConstructor
public class FileController {

    private FileDBService storageService;

    @PostMapping("/upload-post")
    public ResponseEntity<ResponseMessage> uploadPostFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("postId") String postId) { //handles files upload for different posts by getting file and the posID of that file
        String message = "";
        try {
            this.storageService.postStore(file, Long.valueOf(postId));
            message = "File uploaded successfully!" + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/upload-admin") //handles files uploaded by the admins endpoint
    public ResponseEntity<ResponseMessage> adminUploadFileToUser(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("email") String userEmail) {
        String message = "";
        try {
            storageService.adminStore(file, userEmail);
            message = "File uploaded successfully!" + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/upload-user")
    public ResponseEntity<ResponseMessage> uploadUserFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.userStore(file);
            message = "File uploaded successfully!" + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @DeleteMapping("/files/delete/{id}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable("id") String id) {
        this.storageService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/cleanup")
    public ResponseEntity<HttpStatus> cleanup() throws IOException {
        this.storageService.fileCleanup();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
