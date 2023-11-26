package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.dto.customer.EditCustomerNameDto;
import com.codecool.shop.dto.customer.EditCustomerPasswordDto;
import com.codecool.shop.dto.customer.NewCustomerDto;
import com.codecool.shop.repository.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerMapperTest {
    CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    private Customer customer;
    private NewCustomerDto newCustomerDto;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                UUID.randomUUID(),
                "Name",
                "Email@wp.pl",
                LocalDate.of(2012, 12, 12),
                "Password",
                false
        );

        newCustomerDto = new NewCustomerDto(
                "Name",
                "Email@wp.pl",
                "Password"
        );
    }

    @Test
    void testToDto_ShouldReturnCustomerDto_WhenCalled() {
        // when
        CustomerDto expectedCustomerDto = mapper.toDto(customer);

        // then
        assertThat(expectedCustomerDto.id()).isEqualTo(customer.getId());
        assertThat(expectedCustomerDto.name()).isEqualTo(customer.getName());
        assertThat(expectedCustomerDto.email()).isEqualTo(customer.getEmail());
        assertThat(expectedCustomerDto.submissionTime()).isEqualTo(customer.getSubmissionTime());
    }

    @Test
    void testDtoToCustomer_ShouldReturnCustomer_WhenCalled() {
        // when
        Customer expectedCustomer = mapper.dtoToCustomer(newCustomerDto);

        // then
        assertThat(expectedCustomer.getName()).isEqualTo(newCustomerDto.name());
        assertThat(expectedCustomer.getEmail()).isEqualTo(newCustomerDto.email());
        assertThat(expectedCustomer.getPassword()).isEqualTo(newCustomerDto.password());
    }

    @Test
    void testUpdateCustomerNameFromDto_ShouldUpdateExistingCustomerName_WhenCalled() {
        // given
        EditCustomerNameDto editCustomerNameDto = new EditCustomerNameDto("Kacper");
        // when
        mapper.updateCustomerNameFromDto(editCustomerNameDto, customer);

        // then
        assertThat(customer.getName()).isEqualTo(editCustomerNameDto.name());
    }

    @Test
    void testUpdateCustomerPasswordFromDto_ShouldUpdateExistingCustomerPassword_WhenCalled() {
        // given
        EditCustomerPasswordDto editCustomerPasswordDto = new EditCustomerPasswordDto("Kacper");
        // when
        mapper.updateCustomerPasswordFromDto(editCustomerPasswordDto, customer);

        // then
        assertThat(customer.getPassword()).isEqualTo(editCustomerPasswordDto.password());
    }
}
