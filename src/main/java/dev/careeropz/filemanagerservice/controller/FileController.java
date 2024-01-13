package dev.careeropz.filemanagerservice.controller;

import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.exception.ResourceNotFoundException;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import dev.careeropz.filemanagerservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file/api/v1/file-manager")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully. File ID: " + fileId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading the file: " + e.getMessage());
        }
    }

    @GetMapping("/metadata/{fileId}")
    public ResponseEntity<FileMetadataModel> getFileMetadata(@PathVariable String fileId) {
        FileMetadataModel metadata = fileService.getFileMetadata(fileId);
        if (metadata != null) {
            return ResponseEntity.ok(metadata);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try {
            FileContentDto fileContent = fileService.downloadFile(fileId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileContent.getFileName())
                    .body(fileContent.getFileBytes());
        } catch (IOException e) {
            throw new ResourceNotFoundException("File not found with ID: " + fileId);
        }
    }
}
