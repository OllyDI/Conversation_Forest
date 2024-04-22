package com.project.ConversationForest.controller;

import com.project.ConversationForest.domain.Member;
import com.project.ConversationForest.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class memberController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public memberController(MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("login")
    public String loginUser() {
        return "members/login";
    }


    @ResponseBody
    @PostMapping("session_login")
    public String sesionLogin(@RequestParam("email") String email, @RequestParam("pw") String pw) {
        Member member = new Member();
        member.setEmail(email);
        boolean res = true;

        if (email.trim().isEmpty()) {
            res = false;
        }
        if (!memberService.extractedMember(member)) {
            res = false;
        }
        if (res == false) {
            String encodePw = memberService.findPw(email).get().getPw();
            boolean sessionKey = passwordEncoder.matches(pw, encodePw);

            if (sessionKey) {

            }
        }
        return "OK";
    }

    @GetMapping("register")
    public String registerUser() {
        return "members/register";
    }

    @PostMapping("register")
    public String create(MemberForm memberForm) {
        Member member = new Member();
        member.setEmail(memberForm.getEmail());
        member.setPw(passwordEncoder.encode(memberForm.getPw()));
        member.setName(memberForm.getName());
        memberService.join(member);

        return "redirect:/login";
    }

    @PostMapping("duplicate")
    public ResponseEntity<Boolean> duplicate(@RequestParam("email") String email) {
        Member member = new Member();
        member.setEmail(email);
        boolean res = true;

        if (email.trim().isEmpty()) {
            res = false;
        }
        if (!memberService.extractedMember(member)) {
            res = false;
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
