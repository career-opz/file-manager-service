package dev.careeropz.filemanagerservice.controller;

import dev.careeropz.commons.fileservice.dto.responseto.FileUploadResponseDto;
import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.commons.fileservice.dto.requestdto.FileUploadRequestDto;
import dev.careeropz.filemanagerservice.exception.ResourceNotFoundException;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import dev.careeropz.filemanagerservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/file-manager/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<FileUploadResponseDto> uploadFile(@ModelAttribute FileUploadRequestDto fileUploadRequestDto) throws IOException {
        FileUploadResponseDto fileUploadResponse = fileService.uploadFile(fileUploadRequestDto);
        return ResponseEntity.ok(fileUploadResponse);
    }

    @GetMapping("/{fileId}/metadata")
    public ResponseEntity<FileMetadataModel> getFileMetadata(@PathVariable String fileId) {
        FileMetadataModel metadata = fileService.getFileMetadata(fileId);
        if (metadata != null) {
            return ResponseEntity.ok(metadata);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{fileId}")
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

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting the file: " + e.getMessage());
        }
    }
}
