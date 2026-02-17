package br.com.bytestorm.mark_point.entity.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiErrosDTO {

    @Getter
    public List<String> errors;

    public ApiErrosDTO(String messaage) {this.errors = Arrays.asList(messaage);}

    public ApiErrosDTO(List<String> erros) {this.errors = erros;}
}
