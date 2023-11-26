package com.codecool.shop.service;

import com.codecool.shop.dto.address.AddressDto;
import com.codecool.shop.dto.address.EditAddressDto;
import com.codecool.shop.dto.address.NewAddressDto;
import com.codecool.shop.repository.AddressRepository;
import com.codecool.shop.repository.entity.Address;
import com.codecool.shop.repository.entity.Customer;
import com.codecool.shop.service.exception.ObjectNotFoundException;
import com.codecool.shop.service.mapper.AddressMapper;
import com.codecool.shop.service.validator.AddressValidator;
import com.codecool.shop.service.validator.CustomerValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AddressValidator addressValidator;
    private final CustomerValidator customerValidator;

    public AddressDto getAddressByCustomerId(UUID customerId) {
        return addressRepository.findByCustomerId(customerId)
                .map(addressMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException(customerId, Customer.class));
    }

    public void saveNewAddress(NewAddressDto newAddressDto) {
        customerValidator.validateByEntityId(newAddressDto.customerId());
        addressRepository.save(addressMapper.dtoToAddress(newAddressDto));
    }

    public void updateAddress(UUID id, EditAddressDto editAddressDto) {
        Address updatedAddress = addressValidator.validateByEntityId(id);
        addressMapper.updateAddressFromDto(editAddressDto, updatedAddress);
        addressRepository.save(updatedAddress);
    }
}
