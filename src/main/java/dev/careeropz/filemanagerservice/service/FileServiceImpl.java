package dev.careeropz.filemanagerservice.service;


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
    public String uploadFile(MultipartFile file) throws IOException {
        return storageService.upload(file);
    }

    @Override
    public FileMetadataModel getFileMetadata(String fileId) {
        return storageService.getFileMetadata(fileId);
    }

    @Override
    public FileContentDto downloadFile(String fileId) throws IOException {
        return storageService.download(fileId);
    }
}

