package br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.security;

import br.com.rafaelblomer.AnimalsOng_Backend.infrastructure.model.Ong;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserAuthenticated implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final Ong user;

    public UserAuthenticated(Ong user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "read");
    }

    @Override
    public String getPassword() {
        return user.getSenha();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
