package org.redhat.rhdh.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

    @NotBlank
    @Size(max = 100)
    public String firstName;

    @NotBlank
    @Size(max = 100)
    public String lastName;

    @NotBlank
    @Email
    public String email;

    @Size(max = 20)
    public String phone;
}
