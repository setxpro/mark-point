package br.com.bytestorm.mark_point.enums;

public enum TicketItemStatusEnum {

    CONSUMED("Consumido"),
    CANCELED("Cancelado");

    private final String description;

    TicketItemStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
