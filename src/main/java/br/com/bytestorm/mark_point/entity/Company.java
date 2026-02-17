package br.com.bytestorm.mark_point.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String imgUrl;
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_user_id")
    private User owner;

    @OneToMany(
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ticket> tickets = new ArrayList<>();

    public Company(String icon, String imgUrl, String address, String phone, String email, String name) {
        this.icon = icon;
        this.imgUrl = imgUrl;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.name = name;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }
}