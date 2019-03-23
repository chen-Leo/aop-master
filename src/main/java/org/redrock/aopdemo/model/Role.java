package org.redrock.aopdemo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="role_table")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;
    private String roleName;
    private int permissionId;
}
