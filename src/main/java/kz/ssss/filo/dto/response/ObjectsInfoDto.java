package kz.ssss.filo.dto.response;

import io.minio.messages.ResponseDate;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class ObjectsInfoDto {

    String name;

    String path;

    long size;

    ResponseDate modifiedData;

    boolean isFolder;

}
