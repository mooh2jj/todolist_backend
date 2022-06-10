package com.example.todolist_prac.service.user;

import com.example.todolist_prac.dto.user.PrincipalDetails;
import com.example.todolist_prac.model.user.User;
import com.example.todolist_prac.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    // UserController에 UserDetailsService를 구현한 Service 주입시
    // 순환 참조가 일어나!

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        return (UserDetails) new PrincipalDetails(user).getAuthorities();
    }
}
