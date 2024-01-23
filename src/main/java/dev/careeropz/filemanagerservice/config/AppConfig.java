package dev.careeropz.filemanagerservice.config;

import dev.careeropz.filemanagerservice.repository.FileMetadataRepository;
import dev.careeropz.filemanagerservice.service.FileService;
import dev.careeropz.filemanagerservice.service.FileServiceImpl;
import dev.careeropz.filemanagerservice.service.LocalStorageServiceImpl;
import dev.careeropz.filemanagerservice.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final FileMetadataRepository fileMetadataRepository;
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Value("${file.storage.type}")
    private String storageType;

    @Bean
    public FileService fileService(StorageService storageService) {
        return new FileServiceImpl(storageService);
    }

    @Bean
    public StorageService storageService() {
        if ("local".equals(storageType)) {
            return new LocalStorageServiceImpl(fileMetadataRepository);
        } else if ("s3".equals(storageType)) {
//            S3Client s3Client = S3Client.builder()
//                    .region(software.amazon.awssdk.regions.Region.US_EAST_1)
//                    .endpointOverride(URI.create("https://s3.amazonaws.com"))
//                    .build();
//            return new S3StorageServiceImpl(s3Client);
            return null;
        } else {
            throw new IllegalArgumentException("Invalid file storage type: " + storageType);
        }
    }
}