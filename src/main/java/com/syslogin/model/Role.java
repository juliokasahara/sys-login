package com.syslogin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Column
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    public String getAuthority() {
        return this.roleName;
    }
}
