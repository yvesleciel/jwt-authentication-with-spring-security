package org.accescontrol.service.sec.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("attemptAuthentication");
        System.out.println(request.getRequestURL().toString());

        byte[] inputStreamBytes = new byte[0];
        try {
            inputStreamBytes = StreamUtils.copyToByteArray(request.getInputStream());
            System.out.println("****************************** copy to stream");
            System.out.println(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String jsonRequest = new String(inputStreamBytes);
          //  String requestBodyJsonString = jsonRequest.get("body");
            // other code
            System.out.println("**************** req body");
            System.out.println(jsonRequest);
        UserParam userParam = null;
        try {
             userParam = new ObjectMapper().readValue(jsonRequest, UserParam.class);
            System.out.println(userParam.username);
            System.out.println(userParam.password);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //String username = request.getParameter("username");
//        String username = jsonRequest.split("&")[0].split("=")[1];
//        String password = jsonRequest.split("&")[1].split("=")[1];
        String username = userParam.username;
        String password = userParam.password;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username,password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        System.out.println("Successful authentication");
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm1 = Algorithm.HMAC256("mySecret");
        Algorithm algorithm2 = Algorithm.HMAC256("mySecret123");
        String jwtAccessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 5*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", user.getAuthorities().stream().map(ga -> ga.getAuthority()).toList())
                .sign(algorithm1);

        String jwtRefreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", user.getAuthorities().stream().map(ga -> ga.getAuthority()).toList())
                .sign(algorithm2);

        Map<String, String> idToken = new HashMap<>();
        idToken.put("access-token", jwtAccessToken);
        idToken.put("refresh-token", jwtRefreshToken);
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }
}

class UserParam{
   public  String username;
   public  String password;
}
