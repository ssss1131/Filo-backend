package kz.ssss.filo.dto.response;


public record ObjectsInfoResponse(String name, String path, long size, String modifiedData, FileType type) {

}
