package org.example;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Hello world!
 *
 */
public class App  {
    static EntityManagerFactory emfOrder;
    static EntityManagerFactory emfClient;
    static EntityManagerFactory emfGoods;
    static EntityManager emOrder;
    static EntityManager emClient;
    static EntityManager emGoods;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emfOrder = Persistence.createEntityManagerFactory("Orders");
            emfClient = Persistence.createEntityManagerFactory("Clients");
            emOrder = emfOrder.createEntityManager();
            emClient = emfClient.createEntityManager();
            emfGoods = Persistence.createEntityManagerFactory("Goods");
            emGoods = emfGoods.createEntityManager();

            try {
                while (true) {
                    System.out.println("1: add new client");
                    System.out.println("3: add new order");
                    System.out.println("4: add good");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                        addClient(sc);
                            break;
                        case "3":
                        addOrder(sc);
                            break;
                        case "4":
                        addGoods(sc);
                            break;
                        default:
                            return;
                    }
                }

            } finally {
                sc.close();
                emOrder.close();
                emfOrder.close();
                emClient.close();
                emfClient.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }

    private static void addClient(Scanner sc) {
        System.out.print("Enter client name: ");
        String name = sc.nextLine();

        emClient.getTransaction().begin();
        try {
            Client c = new Client(name);
            emClient.persist(c);
            emClient.getTransaction().commit();

            System.out.println(c.getId());
        } catch (Exception ex) {
            emClient.getTransaction().rollback();
        }
    }

    private static void addOrder(Scanner sc) {
        boolean flag = true;
        Client c = null;
        while (flag) {
            try {
                System.out.print("Enter client ID (0- exit): ");
                String sClientID = sc.nextLine();
                Long clientID = Long.parseLong(sClientID);
                if (clientID == 0){
                    return;
                }
                Query query = emClient.createQuery(
                        "SELECT x FROM Client x WHERE x.id = :id", Client.class);
                query.setParameter("id", clientID);
                c = (Client) query.getSingleResult();
                flag = false;
            } catch (NoResultException ex) {
                System.out.println("Client not found!");
            }
        }
        flag = true;
        List<Long> gList = new ArrayList<>();
        while (flag) {
            System.out.print("Enter goods ID (0- end, -1 - exit): ");
            String sGoodsID = sc.nextLine();
            Long goodsID = Long.parseLong(sGoodsID);
            if (goodsID==0){flag = false; break;}
            if (goodsID==-1){
                return;
            }
            Goods g = null;
            try {
                Query query = emGoods.createQuery(
                        "SELECT x FROM Goods x WHERE x.id = :id", Goods.class);
                query.setParameter("id", goodsID);
                g = (Goods) query.getSingleResult();
                gList.add(goodsID);
            } catch (NoResultException ex) {
                System.out.println("Goods not found!");
            }
        }
        if (gList.size() == 0) {
            return;
        }
            Orders o = new Orders(new Date(), c);
            try {
            for ( Long l:gList   ) {
                Query query = emGoods.createQuery(
                        "SELECT g FROM Goods g WHERE id = :id", Goods.class);
                query.setParameter("id", l);
                Goods g = (Goods) query.getSingleResult();
                o.addGoods(g);
            }
            } catch (NoResultException ex) {
                System.out.println("Goods not found!");
            }
        emOrder.getTransaction().begin();
        try {
            emOrder.persist(o);
            emOrder.getTransaction().commit();

        } catch (Exception ex) {
            emOrder.getTransaction().rollback();
            System.out.println("SQL Error");
            ex.printStackTrace();
        }
    }
    private static void addGoods(Scanner sc) {
        System.out.print("Enter goods name: ");
        String name = sc.nextLine();

        emGoods.getTransaction().begin();
        try {
            Goods g = new Goods(name);
            emGoods.persist(g);
            emGoods.getTransaction().commit();

            System.out.println(g.getId());
        } catch (Exception ex) {
            emGoods.getTransaction().rollback();
        }
    }




}



