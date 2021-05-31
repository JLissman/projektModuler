package db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Jonathan
 */
@Entity
public class Pokemon {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Integer id;
    @Basic
    private String name;
    @Basic
    private String evolution;


    public Pokemon() {
    }

    public Pokemon(String name, String evolution) {
        this.name = name;
        this.evolution = evolution;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvolution() {
        return evolution;
    }

    public void setEvolution(String evolution) {
        this.evolution = evolution;
    }

    @Override
    public String toString() {
        return "Pokemon number:" + id + ", name=" + name + ", evolution=" + evolution + '}';
    }
    
    
    
    
}