package plus.fort.itinform.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails getCurrentUser() throws UsernameNotFoundException;
    void saveCurrentUser(UserDetails user);
}
