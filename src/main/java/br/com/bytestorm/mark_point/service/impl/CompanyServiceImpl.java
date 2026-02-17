package br.com.bytestorm.mark_point.service.impl;

import br.com.bytestorm.mark_point.entity.Company;
import br.com.bytestorm.mark_point.entity.User;
import br.com.bytestorm.mark_point.entity.dto.CompanyDTO;
import br.com.bytestorm.mark_point.entity.dto.ListCompanyDTO;
import br.com.bytestorm.mark_point.entity.mapper.CompanyMapper;
import br.com.bytestorm.mark_point.enums.EnumException;
import br.com.bytestorm.mark_point.exception.NegocioException;
import br.com.bytestorm.mark_point.exception.ServiceException;
import br.com.bytestorm.mark_point.repository.CompanyRepository;
import br.com.bytestorm.mark_point.repository.UserRepository;
import br.com.bytestorm.mark_point.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyMapper companyMapper, UserRepository userRepository, CompanyRepository companyRepository) {
        this.companyMapper = companyMapper;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void create(CompanyDTO companyDTO) {
        // Validations
        if (
                companyDTO.name() == null || companyDTO.name().isEmpty() ||
                        companyDTO.email() == null || companyDTO.email().isEmpty() ||
                        companyDTO.phone() == null || companyDTO.phone().isEmpty() ||
                        companyDTO.address() == null || companyDTO.address().isEmpty() ||
                        companyDTO.owner() == null
        ) {
            throw new NegocioException("Por favor, preencha todos os campos.", EnumException.BAD_REQUEST_400);
        }

        User user = userRepository.findById(companyDTO.owner()).orElseThrow(
                () -> new ServiceException("Usuário não encontrado.", EnumException.NOT_FOUND_404)
        );

        Company company = companyMapper.toEntity(companyDTO);
        company.setOwner(user);

        companyRepository.save(company);
    }

    @Override
    public void update(Long id, CompanyDTO companyDTO) {
        if (
        companyDTO.name() == null || companyDTO.name().isEmpty() ||
        companyDTO.email() == null || companyDTO.email().isEmpty() ||
        companyDTO.phone() == null || companyDTO.phone().isEmpty() ||
        companyDTO.address() == null || companyDTO.address().isEmpty()
        ) {
            throw new NegocioException("Por favor, preencha todos os campos.", EnumException.BAD_REQUEST_400);
        }

        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ServiceException("Empresa não encontrada.", EnumException.NOT_FOUND_404)
        );

        company.setName(companyDTO.name());
        company.setEmail(companyDTO.email());
        company.setPhone(companyDTO.phone());
        company.setAddress(companyDTO.address());
        company.setImgUrl(companyDTO.imgUrl());
        company.setIcon(companyDTO.icon());

        companyRepository.save(company);
    }

    @Override
    public void delete(Long id) {
        companyRepository.findById(id).orElseThrow(
                () -> new ServiceException("Empresa não encontrada.", EnumException.NOT_FOUND_404)
        );

        companyRepository.deleteById(id);
    }

    @Override
    public CompanyDTO findOne(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ServiceException("Empresa não encontrada.", EnumException.NOT_FOUND_404)
        );
        return companyMapper.toDto(company);
    }

    @Override
    public List<ListCompanyDTO> list() {
        return companyRepository.findAll().stream().map(companyMapper::toListDto).toList();
    }
}
