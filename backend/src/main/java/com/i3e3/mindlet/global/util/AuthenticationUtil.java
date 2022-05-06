package com.i3e3.mindlet.global.util;

import com.i3e3.mindlet.global.auth.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil {

    public static Long getMemberSeq() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getMember().getSeq();
    }
}
