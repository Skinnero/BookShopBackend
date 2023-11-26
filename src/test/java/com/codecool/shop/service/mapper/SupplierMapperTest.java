package com.codecool.shop.service.mapper;

import com.codecool.shop.dto.supplier.NewSupplierDto;
import com.codecool.shop.dto.supplier.SupplierDto;
import com.codecool.shop.repository.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SupplierMapperTest {
    SupplierMapper mapper = Mappers.getMapper(SupplierMapper.class);

    private UUID supplierId;
    private Supplier supplier;
    private NewSupplierDto newSupplierDto;

    @BeforeEach
    void setUp() {
        supplierId = UUID.randomUUID();

        supplier = new Supplier(
                supplierId,
                "Name",
                "Description"
        );

        newSupplierDto = new NewSupplierDto(
                "Name2",
                "Description2"
        );
    }

    @Test
    void testToDto_ShouldReturnSupplierDto_WhenCalled() {
        // when
        SupplierDto expectedSupplierDto = mapper.toDto(supplier);

        // then
        assertThat(expectedSupplierDto.id()).isEqualTo(supplier.getId());
        assertThat(expectedSupplierDto.name()).isEqualTo(supplier.getName());
        assertThat(expectedSupplierDto.description()).isEqualTo(supplier.getDescription());
    }

    @Test
    void testDtoToSupplier_ShouldReturnSupplier_WhenCalled() {
        // when
        Supplier expectedSupplier = mapper.dtoToSupplier(newSupplierDto);

        // then
        assertThat(expectedSupplier.getName()).isEqualTo(newSupplierDto.name());
        assertThat(expectedSupplier.getDescription()).isEqualTo(newSupplierDto.description());
    }

    @Test
    void testUpdateSupplierFromDto_ShouldUpdateSupplierWithNewValues_WhenCalled() {
        // when
        mapper.updateSupplierFromDto(newSupplierDto, supplier);

        // then
        assertThat(supplier.getName()).isEqualTo(newSupplierDto.name());
        assertThat(supplier.getDescription()).isEqualTo(newSupplierDto.description());
    }
}
