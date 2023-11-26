package com.codecool.shop.service;

import com.codecool.shop.dto.ordertransaction.NewOrderTransactionDto;
import com.codecool.shop.dto.ordertransaction.OrderTransactionDto;
import com.codecool.shop.repository.OrderTransactionRepository;
import com.codecool.shop.repository.entity.OrderTransaction;
import com.codecool.shop.service.mapper.OrderTransactionMapper;
import com.codecool.shop.service.validator.BasketValidator;
import com.codecool.shop.service.validator.CustomerValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderTransactionService {
    private final OrderTransactionRepository orderTransactionRepository;
    private final OrderTransactionMapper orderTransactionMapper;
    private final CustomerValidator customerValidator;
    private final BasketValidator basketValidator;


    public List<OrderTransactionDto> getOrderTransactionByCustomerId(UUID customerId) {
        customerValidator.validateByEntityId(customerId);

        return orderTransactionRepository.findAllByCustomerId(customerId)
                .stream()
                .map(orderTransactionMapper::toDto)
                .toList();
    }

    public void saveNewOrderTransaction(NewOrderTransactionDto newTransactionDto) {
        customerValidator.validateByEntityId(newTransactionDto.customerId());
        basketValidator.validateByEntityId(newTransactionDto.basketId());

        OrderTransaction savedTransaction = orderTransactionMapper.dtoToTransaction(newTransactionDto);
        savedTransaction.setSubmissionTime(LocalDateTime.now());

        orderTransactionRepository.save(savedTransaction);
    }

}
