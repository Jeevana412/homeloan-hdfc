package hdfc.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Product implements Serializable {

    private @Id @GeneratedValue Integer id;

    @Column(nullable = false)
    private String name;

    private Integer price;

    public Product() {}

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
