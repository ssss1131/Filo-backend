package kz.ssss.filo.repository;

import kz.ssss.filo.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileMetaDataRepository extends JpaRepository<FileMetadata, UUID> {

    boolean existsByLogicalPathAndTenantId(String logicalPath, UUID tenantId);

}
