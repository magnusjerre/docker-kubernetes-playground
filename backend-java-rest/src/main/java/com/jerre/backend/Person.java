package com.jerre.backend;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    public Long id;

    @Column(name = "name")
    public String name;

    public Person(){}

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Username{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
