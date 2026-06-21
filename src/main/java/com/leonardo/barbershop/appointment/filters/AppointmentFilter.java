
package com.leonardo.barbershop.appointment.filters;

import com.leonardo.barbershop.appointment.enums.AppointmentStatus;
import com.leonardo.barbershop.appointment.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentFilter {

    public static Specification<Appointment> hasStatus(AppointmentStatus status){
        return (root, query, criteriaBuilder) -> {
            if(status == null){
                return null;
            }

            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Appointment> hasClientName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()){
                return null;
            }

            String target = "%" + name.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("firstName")), target),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("client").get("lastName")), target)
            );
        };
    }

    public static Specification<Appointment> hasEmployeeName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()){
                return null;
            }

            String target = "%" + name.toLowerCase() + "%";

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("employee").get("name")), target);
        };
    }
}
