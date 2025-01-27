package com.noratt.springdatarestrelationshipissueexample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Child {

  @EqualsAndHashCode.Exclude
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int sequence;

  private String childProp1;
  private Boolean childProp2;

  @ManyToOne
  private School school;

}
