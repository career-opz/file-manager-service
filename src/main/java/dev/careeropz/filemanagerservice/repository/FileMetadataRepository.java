package dev.careeropz.filemanagerservice.repository;

import dev.careeropz.filemanagerservice.model.FileMetadataModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileMetadataRepository extends MongoRepository<FileMetadataModel, String> {
    FileMetadataModel findByFileId(String fileId);
}
