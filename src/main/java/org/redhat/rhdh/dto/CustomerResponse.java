package org.redhat.rhdh.dto;

import java.time.Instant;

public class CustomerResponse {

    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public Instant createdAt;
    public Instant updatedAt;
}
