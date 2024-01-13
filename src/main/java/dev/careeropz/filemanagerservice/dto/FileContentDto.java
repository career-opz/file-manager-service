package dev.careeropz.filemanagerservice.dto;

import lombok.Data;

@Data
public class FileContentDto {
    private final String fileName;
    private final byte[] fileBytes;
}
