package org.example;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Orders> orders = new ArrayList<>();

    public Client() {}

    public Client(String name) {
        this.name = name;
    }

    public void addOrder(Orders order) {
        if ( ! orders.contains(order)) {
            orders.add(order);
            order.setClient(this);
        }
    }

    public Long getId() {
        return id;
    }

    public Orders getOrder(int index) {
        return orders.get(index);
    }

    public void clearOrders() {
        orders.clear();
    }

}
