package kz.ssss.filo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(
                        name = "uq_users_tenant_username",
                        columnList = "tenant_id, username",
                        unique = true
                ),
                @Index(
                        name = "uq_users_global_username",
                        columnList = "username",
                        unique = true
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false, columnDefinition = "user_role DEFAULT 'TENANT_USER'")
    private UserRole role = UserRole.TENANT_USER;

    @Builder
    public User(String username, String password, Tenant tenant, UserRole role) {
        this.username = username;
        this.password = password;
        this.tenant   = tenant;
        this.role     = role;
    }
}
