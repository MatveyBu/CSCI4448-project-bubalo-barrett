package barrettbubalo.spotifytracker.repository;

import barrettbubalo.spotifytracker.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByDisplayName(String displayName);
}