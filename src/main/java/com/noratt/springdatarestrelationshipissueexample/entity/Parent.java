package com.noratt.springdatarestrelationshipissueexample.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.collection.spi.PersistentCollection;

/**
 * @author noratt
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Parent {

  @EqualsAndHashCode.Exclude
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String parentProp1;
  private String parentProp2;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @OrderBy("sequence asc")
  @Setter(AccessLevel.NONE)
  @Access(AccessType.PROPERTY)
  private List<Child> children;

  public void setChildren(List<Child> children) {
    if (this.children == null || this.children == children
        || children instanceof PersistentCollection) {
      this.children = children;
    } else {
      this.children.clear();
      this.children.addAll(children);
    }
  }

}
