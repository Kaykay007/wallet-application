//package com.korede.wallet.config;
//
//import com.korede.wallet.entity.User;
//import com.korede.wallet.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Optional;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository; // Assume this is your user repository
//
//    @Autowired
//    public UserDetailsServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Fetch user from the database
//        Optional<User> userOptional = userRepository.findByUsername(username);
//
//        // If user not found, throw exception
//        if (!userOptional.isPresent()) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        User user = userOptional.get(); // Get the user
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                (Collection<? extends GrantedAuthority>) user.getAuthorities() // Assuming you have a method to get authorities
//        );
//    }
//}
