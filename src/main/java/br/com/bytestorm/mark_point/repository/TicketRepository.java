package br.com.bytestorm.mark_point.repository;

import br.com.bytestorm.mark_point.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
