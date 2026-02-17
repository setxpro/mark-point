package br.com.bytestorm.mark_point.entity.mapper;

import br.com.bytestorm.mark_point.entity.Company;
import br.com.bytestorm.mark_point.entity.dto.CompanyDTO;
import br.com.bytestorm.mark_point.entity.dto.ListCompanyDTO;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toEntity(CompanyDTO company) {
        return new Company(
                company.icon(),
                company.imgUrl(),
                company.address(),
                company.phone(),
                company.email(),
                company.name()
        );
    }

    public CompanyDTO toDto(Company company) {
        return new CompanyDTO(
                company.getName(),
                company.getEmail(),
                company.getEmail(),
                company.getAddress(),
                company.getImgUrl(),
                company.getIcon(),
                company.getOwner().getId()
        );
    }

    public ListCompanyDTO toListDto(Company company) {
        return new ListCompanyDTO(
                company.getId(),
                company.getName(),
                company.getEmail(),
                company.getPhone(),
                company.getAddress(),
                company.getImgUrl(),
                company.getIcon(),
                company.getOwner().getId()
        );
    }
}
