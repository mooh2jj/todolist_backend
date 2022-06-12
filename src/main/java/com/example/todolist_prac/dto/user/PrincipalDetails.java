package com.example.todolist_prac.dto.user;

import com.example.todolist_prac.model.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;     // OAuth2를 위한 attributes

    public PrincipalDetails(User user) {		// PrincipalDetails 안에 User 정보를 넣기 위해 생성자에 셋팅!
        this.user = user;
    }

    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
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

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
