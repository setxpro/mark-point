package br.com.bytestorm.mark_point.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

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

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ticket> tickets = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}