package dev.careeropz.filemanagerservice.service;


import dev.careeropz.commons.fileservice.dto.requestdto.FileUploadRequestDto;
import dev.careeropz.commons.fileservice.dto.responseto.FileUploadResponseDto;
import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final StorageService storageService;

    public FileServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public FileUploadResponseDto uploadFile(FileUploadRequestDto fileUploadRequestDto) throws IOException {
        return storageService.upload(fileUploadRequestDto);
    }

    @Override
    public FileMetadataModel getFileMetadata(String fileId) {
        return storageService.getFileMetadata(fileId);
    }

    @Override
    public FileContentDto downloadFile(String fileId) throws IOException {
        return storageService.download(fileId);
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        storageService.delete(fileId);
    }
}

