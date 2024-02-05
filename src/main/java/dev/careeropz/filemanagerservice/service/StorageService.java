package dev.careeropz.filemanagerservice.service;

import dev.careeropz.commons.fileservice.dto.requestdto.FileUploadRequestDto;
import dev.careeropz.commons.fileservice.dto.responseto.FileUploadResponseDto;
import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    FileUploadResponseDto upload(FileUploadRequestDto fileUploadRequestDto) throws IOException;

    FileMetadataModel getFileMetadata(String fileId);

    FileContentDto download(String fileId) throws IOException;

    void delete(String fileId) throws IOException;

}
