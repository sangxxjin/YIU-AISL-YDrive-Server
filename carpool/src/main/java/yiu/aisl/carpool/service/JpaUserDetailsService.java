package yiu.aisl.carpool.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.domain.User;
import yiu.aisl.carpool.repository.UserRepository;
import yiu.aisl.carpool.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username).orElseThrow(
        () -> new UsernameNotFoundException("사용자가 존재하지 않습니다.")
    );
    return new CustomUserDetails(user);
  }
}