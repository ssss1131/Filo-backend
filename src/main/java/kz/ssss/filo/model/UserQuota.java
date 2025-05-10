package kz.ssss.filo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserQuota {

    @Id
    private long userId;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private long usedBytes;

    @Column(nullable = false)
    private long quotaBytes;

}
