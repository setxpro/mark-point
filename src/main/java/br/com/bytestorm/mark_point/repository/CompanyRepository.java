package br.com.bytestorm.mark_point.repository;

import br.com.bytestorm.mark_point.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
