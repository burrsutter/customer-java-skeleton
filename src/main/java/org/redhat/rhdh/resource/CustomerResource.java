package org.redhat.rhdh.resource;

import java.net.URI;
import java.util.List;

import org.redhat.rhdh.dto.CustomerRequest;
import org.redhat.rhdh.dto.CustomerResponse;
import org.redhat.rhdh.dto.PagedResponse;
import org.redhat.rhdh.entity.Customer;
import org.redhat.rhdh.exception.DuplicateEntityException;
import org.redhat.rhdh.exception.NotFoundException;
import org.redhat.rhdh.mapper.CustomerMapper;

import io.quarkus.panache.common.Page;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @GET
    public PagedResponse<CustomerResponse> list(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        List<CustomerResponse> data = Customer.findAll()
                .page(Page.of(page, size))
                .list()
                .stream()
                .map(e -> CustomerMapper.toResponse((Customer) e))
                .toList();

        long total = Customer.count();
        return new PagedResponse<>(data, total, page, size);
    }

    @GET
    @Path("/{id}")
    public CustomerResponse getById(@PathParam("id") Long id) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
        return CustomerMapper.toResponse(customer);
    }

    @POST
    @Transactional
    public Response create(@Valid CustomerRequest request) {
        Customer.findByEmail(request.email).ifPresent(existing -> {
            throw new DuplicateEntityException("Customer with email " + request.email + " already exists");
        });

        Customer customer = CustomerMapper.toEntity(request);
        customer.persist();
        CustomerResponse response = CustomerMapper.toResponse(customer);
        return Response.created(URI.create("/api/v1/customers/" + customer.id))
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public CustomerResponse update(@PathParam("id") Long id, @Valid CustomerRequest request) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }

        Customer.findByEmail(request.email).ifPresent(existing -> {
            if (!existing.id.equals(id)) {
                throw new DuplicateEntityException("Customer with email " + request.email + " already exists");
            }
        });

        customer.firstName = request.firstName;
        customer.lastName = request.lastName;
        customer.email = request.email;
        customer.phone = request.phone;
        return CustomerMapper.toResponse(customer);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
        customer.delete();
        return Response.noContent().build();
    }
}
