package dev.careeropz.filemanagerservice.service;

import dev.careeropz.commons.fileservice.dto.FileType;
import dev.careeropz.commons.fileservice.dto.requestdto.FileUploadRequestDto;
import dev.careeropz.commons.fileservice.dto.responseto.FileUploadResponseDto;
import dev.careeropz.filemanagerservice.dto.FileContentDto;
import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import dev.careeropz.filemanagerservice.repository.FileMetadataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class LocalStorageServiceImpl implements StorageService {

    private final FileMetadataRepository fileMetadataRepository;
    private final ModelMapper modelMapper;
    @Value("${file.storage.local.directory}")
    private String localDirectory;

    public LocalStorageServiceImpl(FileMetadataRepository fileMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.modelMapper = new ModelMapper();
    }

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
        Path fileSaveLocation = getFileSaveLocation(fileUploadRequestDto.getFileType(), fileId, fileUploadRequestDto.getFileName());

        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileSaveLocation.toString());
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileUploadRequestDto.getFile());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }

        FileMetadataModel metadataEntity = FileMetadataModel.builder()
                .fileId(fileId)
                .userId(fileUploadRequestDto.getUserId())
                .fileName(fileUploadRequestDto.getFileName())
                .fileType(fileUploadRequestDto.getFileType())
                .location(fileSaveLocation.toString())
                .uploadedOn(new Date())
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
