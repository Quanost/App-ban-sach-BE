package com.example.LoginService.controller;

import com.example.LoginService.dto.request.SignInForm;
import com.example.LoginService.dto.request.SignUpForm;
import com.example.LoginService.dto.response.JwtResponse;
import com.example.LoginService.dto.response.ResponMessage;
import com.example.LoginService.model.Role;
import com.example.LoginService.model.RoleName;
import com.example.LoginService.model.User;
import com.example.LoginService.security.jwt.JwtProvider;
import com.example.LoginService.security.jwt.JwtTokenFilter;
import com.example.LoginService.security.userpincal.UserPrinciple;
import com.example.LoginService.service.imp.RoleServiceImpl;
import com.example.LoginService.service.imp.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RequestMapping
@RestController
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtTokenFilter jwtTokenFilter;

    // localhost:8080/signup
    //{
    //   "name":"Nguyen Hau",
    //    "username":"abcde",
    //    "email":"e@gmail.com",
    //    "password":"main84",
    //    "roles":["admin"]
    //}
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
        if(userService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponMessage("nouser"), HttpStatus.OK);
        }
        if(userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponMessage("noemail"), HttpStatus.OK);
        }
        if(signUpForm.getAvatar() == null || signUpForm.getAvatar().trim().isEmpty()){
            signUpForm.setAvatar("https://firebasestorage.googleapis.com/v0/b/chinhbeo-18d3b.appspot.com/o/avatar.png?alt=media&token=3511cf81-8df2-4483-82a8-17becfd03211");
        }
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(),passwordEncoder.encode(signUpForm.getPassword()),signUpForm.getAvatar());
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role ->{
            switch (role){
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(
                            ()-> new RuntimeException("Role not found")
                    );
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByName(RoleName.PM).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new ResponMessage("yes"), HttpStatus.OK);
    }

    //localhost:8080/signin
//    {
//        "username":"abcde",
//        "password":"main84"
//    }
    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(),userPrinciple.getAvatar(), userPrinciple.getRoles()));
    }
//    @PutMapping("/change-password")
//    public ResponseEntity<?> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordForm changePasswordForm){
//        String jwt = jwtTokenFilter.getJwt(request);
//        String username = jwtProvider.getUerNameFromToken(jwt);
//        User user;
//        try {
//           user = userService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found wiht -> username"+username));
//           boolean matches = passwordEncoder.matches(changePasswordForm.getCurrentPassword(), user.getPassword());
//           if(changePasswordForm.getNewPassword() != null){
//               if(matches){
//                   user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
//                   userService.save(user);
//               } else {
//                   return new ResponseEntity<>(new ResponMessage("no"), HttpStatus.OK);
//               }
//           }
//           return new ResponseEntity<>(new ResponMessage("yes"), HttpStatus.OK);
//        } catch (UsernameNotFoundException exception){
//            return new ResponseEntity<>(new ResponMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }
//    @PutMapping("/change-avatar")
//    public ResponseEntity<?> changeAvatar(HttpServletRequest httpServletRequest, @Valid @RequestBody ChangeAvatar changeAvatar){
//        String jwt = jwtTokenFilter.getJwt(httpServletRequest);
//        String username = jwtProvider.getUerNameFromToken(jwt);
//        User user;
//        try {
//            if(changeAvatar.getAvatar()==null){
//                return new ResponseEntity(new ResponMessage("no"), HttpStatus.OK);
//            } else{
//                user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Fount with -> username:"+username));
//                user.setAvatar(changeAvatar.getAvatar());
//                userService.save(user);
//            }
//            return new ResponseEntity(new ResponMessage("yes"), HttpStatus.OK);
//        } catch (UsernameNotFoundException exception){
//            return new ResponseEntity<>(new ResponMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }
}
