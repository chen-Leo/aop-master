package org.redrock.aopdemo.model;


import lombok.Data;


import javax.persistence.*;


@Data
@Entity
@Table(name = "ur_table")
public class UrMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ur_id;

    int roleId;

    int userId;
}
