package com.AiNotesGenerator.Security;


import com.AiNotesGenerator.Entity.Note;
import com.AiNotesGenerator.Entity.User;
import com.AiNotesGenerator.Reopsitory.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository = null;  ///chagne

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}