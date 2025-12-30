package Concordia.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;

    private String companyName;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> Items = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> Users = new ArrayList<>();

    public Company() {}

    public Company(Integer companyId, String companyName, List<Item> items, List<User> users) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.Items = items;
        this.Users = users;
    }
    public String getName() { return getCompanyName(); }
    public Integer getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public List<Item> getItems() { return Items; }
    public void setItems(List<Item> items) { Items = items; }
    public List<User> getUsers() { return Users; }
    public void setUsers(List<User> users) { Users = users; }
    public void addItem() { this.Items.add(new Item()); }
}

