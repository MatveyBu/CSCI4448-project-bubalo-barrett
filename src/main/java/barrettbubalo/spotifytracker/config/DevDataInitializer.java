package barrettbubalo.spotifytracker.config;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DevDataInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (accountRepository.findByEmail("test@test.com").isEmpty()) {
            Account account = new Account();
            account.setEmail("test@test.com");
            account.setDisplayName("Test");
            account.setPasswordHash(passwordEncoder.encode("password123"));
            accountRepository.save(account);
            System.out.println("Dev account created");
        } else {
            System.out.println("Dev account already exists");
        }
    }
}