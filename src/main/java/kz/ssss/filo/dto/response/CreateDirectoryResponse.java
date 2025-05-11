package kz.ssss.filo.dto.response;

import lombok.Getter;

@Getter
public class CreateDirectoryResponse {

    String path;

    String name;

    FileType type = FileType.FOLDER;

    public CreateDirectoryResponse(String path, String name) {
        this.path = path;
        this.name = name;
    }
}
