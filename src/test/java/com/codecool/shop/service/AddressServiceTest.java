package com.codecool.shop.service;

import com.codecool.shop.dto.address.AddressDto;
import com.codecool.shop.dto.address.EditAddressDto;
import com.codecool.shop.dto.address.NewAddressDto;
import com.codecool.shop.repository.AddressRepository;
import com.codecool.shop.repository.entity.Address;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.AddressMapper;
import com.codecool.shop.service.validator.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @InjectMocks
    AddressService service;
    @Mock
    AddressRepository repository;
    @Mock
    AddressMapper mapper;
    @Mock
    CustomerValidator customerValidator;

    private UUID addressId;
    private UUID customerId;
    private AddressDto addressDto;
    private NewAddressDto newAddressDto;
    private EditAddressDto editAddressDto;
    private Address address;


    @BeforeEach
    void setUp() {
        addressId = UUID.fromString("783a660e-91ec-42f0-93d3-bbc2bb1484c0");
        customerId = UUID.fromString("b2212e0f-8124-44ce-a8d6-31ac5cfb75cb");

        addressDto = new AddressDto(
                addressId,
                123456L,
                "City",
                "Street",
                "13a",
                ""
        );

        newAddressDto = new NewAddressDto(
                123456L,
                "City",
                "Street",
                "13a",
                "",
                customerId
        );

        editAddressDto = new EditAddressDto(
                123456L,
                "City",
                "Street",
                "13a",
                ""
        );

        address = new Address();

    }

    @Test
    void testGetAddressByCustomerId_ShouldReturnAddressDto_WhenExist() {
        // when
        Mockito.when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(address));
        Mockito.when(mapper.toDto(address)).thenReturn(addressDto);
        AddressDto expectedAddressDto = service.getAddressByCustomerId(customerId);

        // then
        assertThat(expectedAddressDto).isEqualTo(addressDto);
    }

    @Test
    void testGetAddressByCustomerId_ShouldThrowObjectNotFoundException_WhenNoAddress() {
        // when
        Mockito.when(repository.findByCustomerId(customerId)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThatThrownBy(() -> service.getAddressByCustomerId(customerId))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Captor
    ArgumentCaptor<Address> captor;

    @Test
    void testSaveNewAddress_ShouldReturnAddressDto_WhenCustomerExist() {
        // when
        Mockito.when(mapper.dtoToAddress(newAddressDto)).thenReturn(address);
        Mockito.when(repository.save(address)).thenReturn(address);
        service.saveNewAddress(newAddressDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(address);
    }

    @Test
    void testSaveNewAddress_ShouldThrowObjectNotFoundException_WhenNoCustomer() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(customerValidator).validateByEntityId(customerId);

        // then
        assertThatThrownBy(() -> service.saveNewAddress(newAddressDto)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testUpdateAddress_ShouldReturnAddressDto_WhenAddressExist() {
        // when
        Mockito.when(repository.findById(addressId)).thenReturn(Optional.of(address));
        Mockito.when(repository.save(address)).thenReturn(address);
        service.updateAddress(addressId, editAddressDto);

        // then
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(address);
    }

    @Test
    void testUpdateAddress_ShouldThrowObjectNotFoundException_WhenNoAddress() {
        // when
        Mockito.doThrow(ObjectNotFoundException.class).when(repository).findById(addressId);

        // then
        assertThatThrownBy(() -> service.updateAddress(addressId, editAddressDto))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}
