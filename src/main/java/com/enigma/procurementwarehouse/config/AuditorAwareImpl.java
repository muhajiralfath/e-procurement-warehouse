package com.enigma.procurementwarehouse.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        // Get the authentication object from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated and has a username
        if (authentication != null && authentication.isAuthenticated()) {
            // Return the name of the currently logged-in user as the author
            return Optional.of(authentication.getName());
        }
        // If the user is not authenticated or doesn't have a username, you can return a default value
        // or throw an exception based on your application's requirements.
        // Here, we'll return "anonymous" as the default value
        return Optional.of("anonymous");
    }
}
