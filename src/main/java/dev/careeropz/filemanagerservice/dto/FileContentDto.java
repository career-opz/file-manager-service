package dev.careeropz.filemanagerservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileContentDto {
    private final String fileName;
    private final byte[] fileBytes;
}
