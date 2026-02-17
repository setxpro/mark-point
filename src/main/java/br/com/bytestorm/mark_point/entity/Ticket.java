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
@Table(name = "tb_ticket")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "ticket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TicketItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    private Instant dtValidate;

    public void addTicket(TicketItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        item.setTicket(this);
        this.items.add(item);
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
