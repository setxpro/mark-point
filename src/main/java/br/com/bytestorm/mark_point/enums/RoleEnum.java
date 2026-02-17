package br.com.bytestorm.mark_point.enums;

public enum RoleEnum {
    CUSTOMER(1L, "Cliente"),
    BASIC(1L, "Iniciante"),
    ADMIN(2L, "Administrador"),
    SUPPORT(3L, "Suporte"),
    DEVELOPER(4L, "Desenvolvedor"),
    COMPANY(5L, "Empresa");

    private final Long id;
    private final String description;

    RoleEnum(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
