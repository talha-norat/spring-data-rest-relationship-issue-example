package com.noratt.springdatarestrelationshipissueexample.repository;

import com.noratt.springdatarestrelationshipissueexample.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author noratt
 */

public interface ParentRepository extends JpaRepository<Parent, Integer> {

}
