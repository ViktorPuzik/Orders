package org.example;


import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date orderDate;


    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Orders(Date orderDate, Client client) {
        this.orderDate = orderDate;
        this.client = client;
    }

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "goods_id"))
    private Set<Goods> goods = new HashSet<>();


    public void addGoods(Goods good) {
        goods.add(good);
        good.getOrder().add(this);
    }


    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Orders(Client client) {
        this.client = client;
    }


    public Orders() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Set<Goods> goods1) {
        this.goods = goods1;
    }


}
