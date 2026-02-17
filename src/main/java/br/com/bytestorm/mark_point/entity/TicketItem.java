package br.com.bytestorm.mark_point.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "tb_ticket_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(nullable = false)
    private Instant createdAt;

    @JsonIgnore
    public Ticket getTicket() {
        return ticket;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
