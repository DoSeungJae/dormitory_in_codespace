package com.DormitoryBack.domain.auth.domain.security.principal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.DormitoryBack.domain.member.domain.entity.User;

import lombok.Getter;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;
    //private final RoleType role;
    private final OAuth2User oAuth2User;

    public UserPrincipal(User user, OAuth2User oAuth2User){
        this.id = user.getId();
        this.email = user.getEMail();
        this.password = user.getPassWord();
        this.nickname = user.getNickName();
        //this.role = user.getRole();
        this.oAuth2User = oAuth2User;
    }

    @Override
    public String getUsername(){
        return email;
    }

    /*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
    */
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Map<String, Object> getAttributes(){
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName(){
        return oAuth2User.getName();
    }

    

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
