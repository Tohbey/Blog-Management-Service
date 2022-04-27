package com.example.springsecurityjwt.jwt;

import com.example.springsecurityjwt.webConfig.CustomDetail;
import com.example.springsecurityjwt.webConfig.CustomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private CustomDetailService customDetailService;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
        final String authorizationHeader= request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7); //leaving out "Bearer "
            username = jwtTokenUtil.extractUsername(jwt);	//to extract username
        }

        //verifying if username is not null and the security context doesnt have any value
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            CustomDetail userDetails = this.customDetailService.loadUserByUsername(username); //get the userDetails
            //validate the token with userDetails
            if(jwtTokenUtil.validateToken(jwt, userDetails)){
                //create a new username and password authentication token
                //NB: dis will be normal operation for spring security by since we are overriding it we will have to set it up ourselves.
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                //setting the details
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
