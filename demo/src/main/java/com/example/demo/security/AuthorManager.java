package com.example.demo.security;

import com.example.demo.customModels.CustomUserDetails;
import com.example.demo.services.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
@Component
@RequiredArgsConstructor
public class AuthorManager implements AuthorizationManager<Object> {
    private final UserDetailService detailsService;
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, Object object) {
        Authentication authentication = authenticationSupplier.get();
        if (authentication.isAuthenticated()) {
            // Thực hiện logic kiểm tra quyền hạn ở đây
            CustomUserDetails user = loadUser(authentication);
            boolean hasRoleUser = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(user.getAuthorities()));

            // Quyết định xem có cho phép truy cập hay không
            return new AuthorizationDecision(hasRoleUser);
        }
        return new AuthorizationDecision(false);

    }
    private CustomUserDetails loadUser(Authentication auth) {
        return detailsService.loadUserByUsername(auth.getPrincipal().toString());
    }
}
