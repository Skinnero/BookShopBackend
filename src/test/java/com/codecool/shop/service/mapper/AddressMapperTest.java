package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.address.AddressDto;
import com.codecool.shop.dto.address.EditAddressDto;
import com.codecool.shop.dto.address.NewAddressDto;
import com.codecool.shop.repository.entity.Address;
import com.codecool.shop.repository.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class AddressMapperTest {
    AddressMapper mapper = Mappers.getMapper(AddressMapper.class);

    private Address address;
    private NewAddressDto newAddressDto;
    private EditAddressDto editAddressDto;

    @BeforeEach
    void setUp() {
        address = new Address(
                UUID.randomUUID(),
                1L,
                "1",
                "1",
                "1",
                "1",
                new Customer()
        );

        newAddressDto = new NewAddressDto(
                2L,
                "2",
                "2",
                "2",
                "2",
                UUID.randomUUID()
        );

        editAddressDto = new EditAddressDto(
                3L,
                "3",
                "3",
                "3",
                "3"
        );
    }

    @Test
    void testToDto_ShouldReturnAddressDto_WhenCalled() {
        // when
        AddressDto expectedAddressDto = mapper.toDto(address);

        // then
        assertThat(expectedAddressDto.id()).isEqualTo(address.getId());
        assertThat(expectedAddressDto.zipCode()).isEqualTo(address.getZipCode());
        assertThat(expectedAddressDto.city()).isEqualTo(address.getCity());
        assertThat(expectedAddressDto.street()).isEqualTo(address.getStreet());
        assertThat(expectedAddressDto.streetNumber()).isEqualTo(address.getStreetNumber());
        assertThat(expectedAddressDto.additionalInfo()).isEqualTo(address.getAdditionalInfo());
    }

    @Test
    void testDtoToAddress_ShouldReturnAddress_WhenCalled() {
        // when
        Address expectedAddress = mapper.dtoToAddress(newAddressDto);

        // then
        assertThat(expectedAddress.getZipCode()).isEqualTo(newAddressDto.zipCode());
        assertThat(expectedAddress.getCity()).isEqualTo(newAddressDto.city());
        assertThat(expectedAddress.getStreet()).isEqualTo(newAddressDto.street());
        assertThat(expectedAddress.getStreetNumber()).isEqualTo(newAddressDto.streetNumber());
        assertThat(expectedAddress.getAdditionalInfo()).isEqualTo(newAddressDto.additionalInfo());
    }

    @Test
    void testUpdateAddressFromDto_ShouldUpdateFields_WhenCalled() {
        // when
        mapper.updateAddressFromDto(editAddressDto, address);

        // then
        assertThat(address.getZipCode()).isEqualTo(editAddressDto.zipCode());
        assertThat(address.getCity()).isEqualTo(editAddressDto.city());
        assertThat(address.getStreet()).isEqualTo(editAddressDto.street());
        assertThat(address.getStreetNumber()).isEqualTo(editAddressDto.streetNumber());
        assertThat(address.getAdditionalInfo()).isEqualTo(editAddressDto.additionalInfo());
    }
}
