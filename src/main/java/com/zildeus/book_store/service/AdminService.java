package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.BalanceDto;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.model.ApplicationUser;
import com.zildeus.book_store.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ApplicationUserRepository userRepository;
    public void AddBalance(BalanceDto balanceDto){
        ApplicationUser user = userRepository.findByUsername(balanceDto.username()).orElseThrow(
                ()->new ResourceNotFoundException("user with username %s not found".formatted(balanceDto.username()))
        );
        user.setBalance(balanceDto.amount()+user.getBalance());
        userRepository.save(user);
    }

}
