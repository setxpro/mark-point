package br.com.bytestorm.mark_point.resource;

import br.com.bytestorm.mark_point.entity.dto.CompanyDTO;
import br.com.bytestorm.mark_point.entity.dto.ListCompanyDTO;
import br.com.bytestorm.mark_point.service.CompanyService;
import br.com.bytestorm.mark_point.utils.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Const.COMPANY)
public class CompanyResource {

    private final CompanyService companyService;

    public CompanyResource(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CompanyDTO companyDTO) {
        companyService.create(companyDTO);
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping
    public ResponseEntity<List<ListCompanyDTO>> findAll() {
        return ResponseEntity.ok().body(companyService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(companyService.findOne(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CompanyDTO companyDTO) {
        companyService.update(id, companyDTO);
        return ResponseEntity.ok().body("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        companyService.delete(id);
        return ResponseEntity.ok().body("OK");
    }


}
