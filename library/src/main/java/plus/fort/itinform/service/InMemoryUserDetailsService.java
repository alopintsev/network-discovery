package plus.fort.itinform.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private UserDetails currentUser = null;

    @Override
    public UserDetails getCurrentUser() {
        if (currentUser == null) {
            throw new UsernameNotFoundException("Current user is unknown");
        }
        return currentUser;
    }

    @Override
    public void saveCurrentUser(UserDetails user) {
        currentUser = user;
    }

}
