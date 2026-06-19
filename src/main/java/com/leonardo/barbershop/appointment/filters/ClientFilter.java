package com.leonardo.barbershop.appointment.filters;

import com.leonardo.barbershop.appointment.model.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientFilter {

    public static Specification<Client> hasName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()){
                return null;
            }

            String target = "%" + name.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), target),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), target)
            );
        };
    }

    public static Specification<Client> hasActive(Boolean active){
        return (root, query, criteriaBuilder) -> {
            if(active == null){
                return null;
            }

            return criteriaBuilder.equal(root.get("active"), active);
        };
    }
}
