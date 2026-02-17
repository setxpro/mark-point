package br.com.bytestorm.mark_point.entity;

import br.com.bytestorm.mark_point.enums.TicketItemStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tb_ticket_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketItemStatusEnum statusItem;

    @PrePersist
    public void prePersist() {
        if (statusItem == null) statusItem = TicketItemStatusEnum.CONSUMED;
    }
}
