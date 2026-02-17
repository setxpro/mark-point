package br.com.bytestorm.mark_point.enums;

public enum TicketStatusEnum {
    OPEN("Aberto"),
    CLOSED("Fechado"),
    IN_PROGRESS("Em andamento"),
    EXPIRED("Expirado");

    private final String description;

    TicketStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
