package org.redrock.aopdemo.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.*;

@Data
@Entity
@Table(name ="user_table")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int userId;
   private String userName;
   private String userPassword;

   public boolean IsSuper(int roleId) {
      return 6 == roleId;
   }

}
