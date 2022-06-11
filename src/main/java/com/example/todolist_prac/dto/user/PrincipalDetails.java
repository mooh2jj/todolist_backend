package com.example.todolist_prac.dto.user;

import com.example.todolist_prac.model.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {		// PrincipalDetails 안에 User 정보를 넣기 위해 생성자에 셋팅!
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    // PrincipalDetails 커스터마이징은 getter에도 있음.
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
//        return false(Default)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
//        return false(Default)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
//        return false(Default)
    }

    @Override
    public boolean isEnabled() {
        return true;
//        return false(Default)
    }
}
