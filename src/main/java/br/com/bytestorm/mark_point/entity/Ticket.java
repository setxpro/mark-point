package br.com.bytestorm.mark_point.entity;

import br.com.bytestorm.mark_point.enums.TicketStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_ticket")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "ticket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TicketItem> items = new ArrayList<>();

    private Instant dtValidate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatusEnum statusTicket;

    public void addItem(TicketItem item) {
        item.setTicket(this);
        this.items.add(item);
    }

    public boolean isExpired(Instant now) {
        return dtValidate != null && now.isAfter(dtValidate);
    }

    public void refreshStatusIfExpired(Instant now) {
        if (statusTicket != TicketStatusEnum.CLOSED && isExpired(now)) {
            statusTicket = TicketStatusEnum.EXPIRED;
        }
    }

    @PrePersist
    public void prePersist() {
        if (statusTicket == null) statusTicket = TicketStatusEnum.OPEN;
    }
}
