package dev.careeropz.filemanagerservice.service;

import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import dev.careeropz.filemanagerservice.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class LocalStorageServiceImpl implements StorageService {

    private final FileMetadataRepository fileMetadataRepository;

    @Value("${file.storage.local.directory}")
    private String localDirectory;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String fileId = generateFileId();
        Path destinationPath = Path.of(localDirectory, fileId + "-" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        FileMetadataModel metadataEntity = new FileMetadataModel(fileId, file.getOriginalFilename(), destinationPath.toString());
        fileMetadataRepository.save(metadataEntity);

        return fileId;
    }

    @Override
    public FileMetadataModel getFileMetadata(String fileId) {
        FileMetadataModel metadataEntity = fileMetadataRepository.findByFileId(fileId);
        if (metadataEntity != null) {
            return new FileMetadataModel(metadataEntity.getFileId(), metadataEntity.getFileName(), metadataEntity.getLocation());
        } else {
            return null;
        }
    }

    @Override
    public FileContentDto download(String fileId) throws IOException {
        FileMetadataModel metadataEntity = fileMetadataRepository.findByFileId(fileId);
        if (metadataEntity != null) {
            byte[] fileBytes = Files.readAllBytes(Path.of(metadataEntity.getLocation()));
            return new FileContentDto(metadataEntity.getFileName(), fileBytes);
        } else {
            return null;
        }
    }

    @Override
    public void delete(String fileId) throws IOException {
        FileMetadataModel metadataEntity = fileMetadataRepository.findByFileId(fileId);
        if (metadataEntity != null) {
            Files.delete(Path.of(metadataEntity.getLocation()));
            fileMetadataRepository.delete(metadataEntity);
        }
    }

    private String generateFileId() {
        return java.util.UUID.randomUUID().toString();
    }
}
