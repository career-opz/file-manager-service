package dev.careeropz.filemanagerservice.service;

import dev.careeropz.commons.fileservice.dto.FileType;
import dev.careeropz.commons.fileservice.dto.requestdto.FileUploadRequestDto;
import dev.careeropz.commons.fileservice.dto.responseto.FileUploadResponseDto;
import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import dev.careeropz.filemanagerservice.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class LocalStorageServiceImpl implements StorageService {

    private final FileMetadataRepository fileMetadataRepository;
    private final ModelMapper modelMapper;

    public LocalStorageServiceImpl(FileMetadataRepository fileMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.modelMapper = new ModelMapper();
    }

    @Value("${file.storage.local.directory}")
    private String localDirectory;

    private static String getFileSaveFolder(FileType fileType) {
        return switch (fileType) {
            case CV -> "cv";
            case COVER_LETTER -> "cover";
            case PROFILE_PICTURE -> "pics";
            case OTHER -> "other";
        };
    }

    @Override
    public FileUploadResponseDto upload(FileUploadRequestDto fileUploadRequestDto) throws IOException {
        String fileId = generateFileId();
        Path fileSaveLocation = getFileSaveLocation(fileUploadRequestDto.getFileType(), fileId, fileUploadRequestDto.getFile().getOriginalFilename());
        Files.copy(fileUploadRequestDto.getFile().getInputStream(), fileSaveLocation, StandardCopyOption.REPLACE_EXISTING);

        FileMetadataModel metadataEntity = FileMetadataModel.builder()
                .fileId(fileId)
                .userId(fileUploadRequestDto.getUserId())
                .fileName(fileUploadRequestDto.getFile().getOriginalFilename())
                .fileType(fileUploadRequestDto.getFileType())
                .location(fileSaveLocation.toString())
                .uploadedOn(LocalDateTime.now())
                .build();
        return modelMapper.map(fileMetadataRepository.save(metadataEntity), FileUploadResponseDto.class);
    }

    private Path getFileSaveLocation(FileType fileType, String fileId, String fileName) {
        return Path.of(localDirectory, getFileSaveFolder(fileType), fileId + "-" + fileName);
    }

    @Override
    public FileMetadataModel getFileMetadata(String fileId) {
        return fileMetadataRepository.findByFileId(fileId);
    }

    @Override
    public FileContentDto download(String fileId) throws IOException {
        FileMetadataModel metadataEntity = fileMetadataRepository.findByFileId(fileId);
        if (metadataEntity != null) {
            byte[] fileBytes = Files.readAllBytes(Path.of(metadataEntity.getLocation()));
            return FileContentDto.builder()
                    .fileName(metadataEntity.getFileName())
                    .fileBytes(fileBytes)
                    .build();
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
