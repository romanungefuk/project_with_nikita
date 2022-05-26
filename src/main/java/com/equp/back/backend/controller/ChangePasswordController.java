package com.equp.back.backend.controller;

import com.equp.back.backend.model.User;
import com.equp.back.backend.security.jwt.JwtTokenProvider;
import com.equp.back.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Controller
@Slf4j
public class ChangePasswordController {

    private final UserService userService;
    private final JavaMailSender emailSender;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public ChangePasswordController(UserService userService, JavaMailSender emailSender, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.emailSender = emailSender;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/")
    public String aboutPageOpen(){
        return "about";
    }

    @GetMapping("/password_change")
    public String changePassword(Model model, @RequestParam(value = "email", required = false) String email,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "id", required = false) Long id){
        log.info("Пользователь "+id+" с email: "+email+" зашел на страницу сброса пароля");
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("id", id);
        model.addAttribute("message","");
        return "change_password_page";
    }
    @PostMapping("/password_change")
    public String changePasswordOperation(Model model, @RequestParam(value = "password", required = false) String password,
                                          @RequestParam(value = "password_repeat", required = false) String passwordRepeat,
                                          @RequestParam(value = "id", required = false) Long id,
                                          @RequestParam(value = "email", required = false) String email,
                                          @RequestParam(value = "name", required = false) String name) throws MessagingException {

        User user = userService.findById(id);


        if (password.length() < 4 || passwordRepeat.length() < 4){
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            model.addAttribute("id", id);
            model.addAttribute("message","Слабый пароль");
        }

        else if (!password.equals(passwordRepeat)){
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            model.addAttribute("id", id);
            model.addAttribute("message","Пароли не совпадают");
        }

        else if (password.equals(passwordRepeat)){
            String encryptedPassword = jwtTokenProvider.passwordEncoder().encode(password);
            userService.update(user,encryptedPassword);

            MimeMessage message = emailSender.createMimeMessage();
            boolean multipart = true;
            MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
            String htmlMsg = "<!DOCTYPE html>"+
                    "<html lang=\"ru\">"+
                    "<head>"+
                    "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                    "<title>Title</title>"+
                    "</head>"+
                    "<body>"+
                    "<p>Пароль в приложении успешно изменён.\n" +
                    "\n" +
                    "Ваша команда \"Экви\"</p>"+
                    "</body>"+
                    "</html>";

            message.setContent(htmlMsg, "text/html; charset=utf-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Пароль был изменен");
            helper.setFrom("no-reply@eq-up.ru");

            this.emailSender.send(message);

            return "password_changed_successfully";
        }
        return "change_password_page";

    }
}
