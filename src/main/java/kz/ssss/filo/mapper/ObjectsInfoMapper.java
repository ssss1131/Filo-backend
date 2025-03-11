package kz.ssss.filo.mapper;

import io.minio.messages.Item;
import kz.ssss.filo.dto.response.ObjectsInfoResponse;
import kz.ssss.filo.util.PathUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {PathUtil.class})
public interface ObjectsInfoMapper {

    @Mapping(target = "path", expression = "java(PathUtil.removeUserPrefix(item.objectName(), userId))")
    @Mapping(target = "name", expression = "java(PathUtil.removePathFromName(item.objectName()))")
    @Mapping(target = "size", expression = "java(item.size())")
    @Mapping(target = "modifiedData", expression = "java(new ResponseDate(item.lastModified()))")
    @Mapping(target = "isFolder", expression = "java(item.isDir())")
    ObjectsInfoResponse toDto(Item item, Long userId);
}

