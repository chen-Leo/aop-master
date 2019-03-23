package org.redrock.aopdemo.dao;

import org.redrock.aopdemo.model.UrMapping;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UrMappingRepository extends JpaRepository<UrMapping, Integer> {

    UrMapping findByUserId(int userId);
}
