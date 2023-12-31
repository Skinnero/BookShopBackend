package com.codecool.shop.service;

import com.codecool.shop.config.jwt.repository.entity.CustomerRole;
import com.codecool.shop.config.jwt.repository.entity.Role;
import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.dto.customer.EditCustomerNameDto;
import com.codecool.shop.dto.customer.EditCustomerPasswordDto;
import com.codecool.shop.dto.customer.NewCustomerDto;
import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.exception.EmailNotFoundException;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.CustomerMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder encoder;

    public CustomerDto getCustomerById(UUID id) {
        return customerMapper.toDto(customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Customer.class)));
    }

    public CustomerDto getCustomerByEmail(String email) {
        return customerMapper.toDto(customerRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email)));
    }

    public void saveCustomer(NewCustomerDto newCustomerDto) {
        Customer customerToSave = customerMapper.dtoToCustomer(newCustomerDto);
        assignRole(customerToSave);
        encodePassword(customerToSave);
        customerToSave.setSubmissionTime(LocalDate.now());
        customerRepository.save(customerToSave);
    }

    public void updateCustomerName(UUID id, EditCustomerNameDto editCustomerNameDto) {
        Customer updatedCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Customer.class));
        customerMapper.updateCustomerNameFromDto(editCustomerNameDto, updatedCustomer);
        customerRepository.save(updatedCustomer);
    }

    public void updateCustomerPassword(UUID id, EditCustomerPasswordDto editCustomerPasswordDto) {
        Customer updatedCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Customer.class));
        customerMapper.updateCustomerPasswordFromDto(editCustomerPasswordDto, updatedCustomer);
        encodePassword(updatedCustomer);
        customerRepository.save(updatedCustomer);
    }

    public void softDeleteCustomer(UUID id) {
        Customer customerToDelete = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, Customer.class));
        obfuscateData(customerToDelete);
        customerRepository.save(customerToDelete);
    }

    private void obfuscateData(Customer customerToDelete) {
        int nameLength = customerToDelete.getName().length();
        customerToDelete.setName("*".repeat(nameLength));

        String emailSuffix = customerToDelete.getEmail().split("@")[1];
        customerToDelete.setEmail(UUID.randomUUID() + "@" + emailSuffix);

        customerToDelete.setIsDeleted(true);
    }

    private void assignRole(Customer customerToSave) {
        Role role = new Role();
        role.setCustomerRole(CustomerRole.ROLE_USER);
        customerToSave.assignRoleToCustomer(role);
    }

    private void encodePassword(Customer customerToSave) {
        customerToSave.setPassword(encoder.encode(customerToSave.getPassword()));
    }
}
