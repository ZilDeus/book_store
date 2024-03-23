package com.zildeus.book_store.config.user;

import com.zildeus.book_store.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplicationUserDetailsService implements UserDetailsService {
    private final ApplicationUserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("No User with the supplied username could be found"));
    }
}
