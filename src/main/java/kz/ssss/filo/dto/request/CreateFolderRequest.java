package kz.ssss.filo.dto.request;

import lombok.Value;

@Value
public class CreateFolderRequest {

    private Long userId;
    private String path;
    private String name;

}
