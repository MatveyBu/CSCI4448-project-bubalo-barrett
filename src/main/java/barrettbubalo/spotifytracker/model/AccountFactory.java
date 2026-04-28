package barrettbubalo.spotifytracker.model;

public class AccountFactory {

    public Account createAccount(String displayName, String email, String passwordHash) {
        return new Account(displayName, email, passwordHash);
    }
}