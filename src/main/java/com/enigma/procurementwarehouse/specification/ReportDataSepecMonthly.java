package com.enigma.procurementwarehouse.specification;

import com.enigma.procurementwarehouse.entity.ReportData;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDataSepecMonthly {
    public static Specification<ReportData> getSpecification(String dateNow, Integer month, Integer year) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            LocalDate date = LocalDate.now();

            if (dateNow.equalsIgnoreCase("now")) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder
                        .function("DATE", LocalDate.class, root.get("date")), date));
            }

            if (month != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder
                        .function("MONTH", Integer.class, root.get("date")), month));
            }
            if (year != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder
                        .function("YEAR", Integer.class, root.get("orderDate")), year));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }

}
