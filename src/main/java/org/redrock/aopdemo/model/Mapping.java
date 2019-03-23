package org.redrock.aopdemo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permission_table")
public class Mapping {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int permissionId;
    private String permissionDescribe;

}
