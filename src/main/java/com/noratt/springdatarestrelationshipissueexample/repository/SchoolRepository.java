package com.noratt.springdatarestrelationshipissueexample.repository;

import com.noratt.springdatarestrelationshipissueexample.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author noratt
 */

@RepositoryRestResource
public interface SchoolRepository extends JpaRepository<School, Long> {

}
