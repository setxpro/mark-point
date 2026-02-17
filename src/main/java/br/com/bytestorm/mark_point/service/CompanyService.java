package br.com.bytestorm.mark_point.service;

import br.com.bytestorm.mark_point.entity.dto.CompanyDTO;
import br.com.bytestorm.mark_point.entity.dto.ListCompanyDTO;

import java.util.List;

public interface CompanyService {
    void create(CompanyDTO companyDTO);
    void update(Long id, CompanyDTO companyDTO);
    void delete(Long id);
    CompanyDTO findOne(Long id);
    List<ListCompanyDTO> list();
}
