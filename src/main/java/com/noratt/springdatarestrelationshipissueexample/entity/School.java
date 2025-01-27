package com.noratt.springdatarestrelationshipissueexample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author noratt
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class School {

  @EqualsAndHashCode.Exclude
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String schoolProp1;
  private String schoolProp2;

}
