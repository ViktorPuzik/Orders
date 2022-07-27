package org.example;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Goods")

public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    @ManyToMany(mappedBy = "goods")
    private Set<Orders> order = new HashSet<>();

    public Goods(String name) {
        this.name = name;
    }

    public Goods() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Orders> getOrder() {
        return order;
    }

    public void setOrder(Set<Orders> order) {
        this.order = order;
    }

    public Set<Orders> getOrders() {
        return order;
    }

    public void addOrder(Orders orders) {
        order.add(orders);
        orders.getGoods().add(this);
    }



}
