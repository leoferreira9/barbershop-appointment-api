package com.leonardo.barbershop.appointment.filters;

import com.leonardo.barbershop.appointment.model.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeFilter {

    public static Specification<Employee> hasName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()){
                return null;
            }

            String target = "%" + name.toLowerCase() + "%";

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), target);
        };
    }

    public static Specification<Employee> hasActive(Boolean active){
        return (root, query, criteriaBuilder) -> {
            if(active == null){
                return null;
            }

            return criteriaBuilder.equal(root.get("active"), active);
        };
    }
}
