package com.sprta.samsike.domain.member;

import com.sprta.samsike.domain.Stamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(catalog = "samsike", name = "p_user")
public class Member extends Stamped {
    @Id
    @Column(length = 50, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String role = "ROLE_CUSTOMER";

    public Member() {
    }

    public Member(String username, String password, String name, String email, MemberRoleEnum roleEnum) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = roleEnum.getAuthority();
        this.status = "ACTIVE";
    }

    public void softDelete() {
        this.status = "DELETED";
    }


}
