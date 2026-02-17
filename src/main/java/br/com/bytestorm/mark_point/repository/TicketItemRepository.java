package br.com.bytestorm.mark_point.repository;

import br.com.bytestorm.mark_point.entity.TicketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketItemRepository extends JpaRepository<TicketItem, UUID> {
}
