package dev.careeropz.filemanagerservice.service;

import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file) throws IOException;

    FileMetadataModel getFileMetadata(String fileId);

    FileContentDto downloadFile(String fileId) throws IOException;
}
