package com.codecool.shop.service;

import com.codecool.shop.dto.customer.CustomerDto;
import com.codecool.shop.dto.customer.EditCustomerNameDto;
import com.codecool.shop.dto.customer.EditCustomerPasswordDto;
import com.codecool.shop.dto.customer.NewCustomerDto;
import com.codecool.shop.repository.CustomerRepository;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.exception.EmailNotFoundException;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.CustomerMapper;
import com.codecool.shop.service.validator.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    CustomerService service;
    @Mock
    CustomerRepository repository;
    @Mock
    CustomerMapper mapper;
    @Mock
    CustomerValidator validator;
    @Mock
    PasswordEncoder encoder;

    private UUID customerId;
    private CustomerDto customerDto;
    private NewCustomerDto newCustomerDto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerId = UUID.fromString("b2212e0f-8124-44ce-a8d6-31ac5cfb75cb");

        customerDto = new CustomerDto(
                customerId,
                "Name",
                "Email@wp.pl",
                LocalDate.of(2012, 12, 12)
        );

        newCustomerDto = new NewCustomerDto(
                "Name",
                "Email@wp.pl",
                "Password"
        );

        customer = new Customer(
                customerId,
                "Name",
                "Email@wp.pl",
                LocalDate.of(2012, 12, 12),
                "Password",
                false
        );

    }

    @Test
    void testGetCustomerById_ShouldReturnCustomerDto_WhenExist() {
        // when
        Mockito.when(validator.validateByEntityId(customerId)).thenReturn(customer);
        Mockito.when(mapper.toDto(customer)).thenReturn(customerDto);
        CustomerDto expectedCustomerDto = service.getCustomerById(customerId);

        // then
        assertThat(expectedCustomerDto).isEqualTo(customerDto);
    }

    @Test
    void testGetCustomerById_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(validator.validateByEntityId(customerId)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.getCustomerById(customerId)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testGetCustomerByEmail_ShouldReturnCustomerDto_WhenExist() {
        // when
        Mockito.when(validator.validateByEmail(anyString())).thenReturn(customer);
        Mockito.when(mapper.toDto(customer)).thenReturn(customerDto);
        CustomerDto expectedCustomerDto = service.getCustomerByEmail("Email");

        // then
        assertThat(expectedCustomerDto).isEqualTo(customerDto);
    }

    @Test
    void testGetCustomerByEmail_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(validator.validateByEmail(anyString())).thenThrow(EmailNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.getCustomerByEmail("Email")).isInstanceOf(EmailNotFoundException.class);
    }

    @Captor
    ArgumentCaptor<Customer> captor;

    @Test
    void testSaveCustomer_ShouldReturnCustomerDtoWithUpdatedValues_WhenCalled() {
        // given
        LocalDate date = customer.getSubmissionTime();

        // when
        Mockito.when(mapper.dtoToCustomer(newCustomerDto)).thenReturn(customer);
        Mockito.when(repository.save(customer)).thenReturn(customer);
        service.saveCustomer(newCustomerDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getSubmissionTime()).isNotEqualTo(date);
    }

    @Test
    void testUpdateCustomerName_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(validator.validateByEntityId(customerId))
                .thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.updateCustomerName(customerId, new EditCustomerNameDto("Kacper")))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testUpdateCustomerPassword_ShouldChangeNameAndSave_WhenCalled() {
        // when
        Mockito.when(validator.validateByEntityId(customerId)).thenReturn(customer);
        service.updateCustomerPassword(customerId, new EditCustomerPasswordDto("Kacper"));

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(customer.getPassword());
    }

    @Test
    void testUpdateCustomerPassword_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(validator.validateByEntityId(customerId)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.updateCustomerPassword(customerId, new EditCustomerPasswordDto("Kacper")))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testSoftDeletedCustomer_ShouldObfuscateCustomer_WhenExist() {
        // when
        Mockito.when(validator.validateByEntityId(customerId)).thenReturn(customer);
        service.softDeleteCustomer(customerId);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getName()).isNotEqualTo("Name");
        assertThat(captor.getValue().getEmail()).isNotEqualTo("Email@wp.pl");
        assertThat(captor.getValue().getIsDeleted()).isTrue();
    }

    @Test
    void testSoftDeletedCustomer_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.when(validator.validateByEntityId(customerId)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.softDeleteCustomer(customerId)).isInstanceOf(ObjectNotFoundException.class);
    }
}
