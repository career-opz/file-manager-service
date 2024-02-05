package dev.careeropz.filemanagerservice.service;

import dev.careeropz.commons.fileservice.dto.requestdto.FileUploadRequestDto;
import dev.careeropz.commons.fileservice.dto.responseto.FileUploadResponseDto;
import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileUploadResponseDto uploadFile(FileUploadRequestDto fileUploadRequestDto) throws IOException;

    FileMetadataModel getFileMetadata(String fileId);

    FileContentDto downloadFile(String fileId) throws IOException;

    void deleteFile(String fileId) throws IOException;
}
