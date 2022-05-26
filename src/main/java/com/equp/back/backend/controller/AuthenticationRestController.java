package com.equp.back.backend.controller;


import com.equp.back.backend.model.User;
import com.equp.back.backend.security.jwt.JwtTokenProvider;
import com.equp.back.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 *
 * @author Roman Ungefuk
 * @version 1.0
 */

@RestController
@Slf4j
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    /**
     * Авторизация действующего пользователя
     *
     * @param email
     * @param password
     * @return
     */
    @GetMapping("/api/v1/auth")
    public ResponseEntity auth(@RequestParam(name = "email") String email,
                               @RequestParam(name = "password") String password) {

        Map<Object, Object> response = new HashMap<>();
        User user = userService.findByEmail(email.toLowerCase());
        boolean isPasswordCorrect = false;
        if (user != null) {
            isPasswordCorrect = new BCryptPasswordEncoder().matches(password, user.getPassword());
            log.info("isPasswordCorrect" + isPasswordCorrect + "password " + password + "user.getPassword()" + user.getPassword());
        }

        if (user == null) {
            response.put("codeResponse", 404);
            response.put("message", "Пользователь с такими email не найден");
            log.info(response.toString());
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        } else if (!isPasswordCorrect) {
            response.put("codeResponse", 401);
            response.put("message", "Неверный пароль");
            log.info(response.toString());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } else {
            String token = jwtTokenProvider.createToken(email, user.getRoles());
            response.put("id", user.getId());
            response.put("token", token);
            response.put("message", "Пользователь найден");
            response.put("codeResponse", 201);
            response.put("user", user);
            log.info(response.toString());
            return new ResponseEntity(response, HttpStatus.FOUND);
        }
    }

    /**
     * Проверка сервиса. Метод используется в мониторинге zabbix
     *
     * @return ок, при условии что зарос аутентификации отработает
     * для тестового пользователя верно. Иначе аутентификация не сработает,
     * а должна. Это и послужит для оповещения в электронном сообщении,
     * что сервер не работает
     */
    @GetMapping("/api/v1/auth/check")
    public String check() {
        String result="";
        User user = userService.findByEmail("user@mail.ru");
        boolean isPasswordCorrect = new BCryptPasswordEncoder().matches("12345", user.getPassword());
        if (user != null && isPasswordCorrect) {
            log.info("It is ok. The backend is running.");
            result = "ok";
        } else {
            log.info("It is not ok. The backend is not running.");
            result = "NOT_OK";
        }
        log.info(result);
        return result;
    }
}