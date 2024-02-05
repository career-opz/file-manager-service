package dev.careeropz.filemanagerservice.model;

import dev.careeropz.commons.fileservice.dto.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "file_metadata")
public class FileMetadataModel {

    @Id
    private String fileId;
    private String userId;
    private FileType fileType;
    private String fileName;
    private String location;
    private LocalDateTime uploadedOn;
}
