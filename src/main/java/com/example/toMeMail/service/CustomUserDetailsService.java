package com.example.toMeMail.service;

import com.example.toMeMail.entity.User;
//import com.example.toMeMail.exception.UserNotFoundException;
import com.example.toMeMail.repository.UserRepository;
import com.example.toMeMail.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));  // Use Spring Security's exception

        return new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole());
    }
}
