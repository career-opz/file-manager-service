package dev.careeropz.filemanagerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "file_metadata")
public class FileMetadataModel {

    @Id
    private String fileId;
    private String fileName;
    private String location;
}
