package kz.ssss.filo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Tenant implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, updatable = false, nullable = false)
    private String name;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 500")
    private long quotaMegabytes = 500;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;


    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Builder
    public Tenant(String name, String displayName, long quotaMegabytes){
        this.name = name;
        this.displayName = displayName;
        this.quotaMegabytes = quotaMegabytes;
    }


}
