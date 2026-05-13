package org.redhat.rhdh.mapper;

import org.redhat.rhdh.dto.CustomerRequest;
import org.redhat.rhdh.dto.CustomerResponse;
import org.redhat.rhdh.entity.Customer;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static Customer toEntity(CustomerRequest dto) {
        Customer customer = new Customer();
        customer.firstName = dto.firstName;
        customer.lastName = dto.lastName;
        customer.email = dto.email;
        customer.phone = dto.phone;
        return customer;
    }

    public static CustomerResponse toResponse(Customer entity) {
        CustomerResponse response = new CustomerResponse();
        response.id = entity.id;
        response.firstName = entity.firstName;
        response.lastName = entity.lastName;
        response.email = entity.email;
        response.phone = entity.phone;
        response.createdAt = entity.createdAt;
        response.updatedAt = entity.updatedAt;
        return response;
    }
}
