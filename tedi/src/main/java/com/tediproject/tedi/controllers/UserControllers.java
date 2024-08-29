package com.tediproject.tedi.controllers;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tediproject.tedi.dto.BoolChangeDto;
import com.tediproject.tedi.dto.EmailChangeDto;
import com.tediproject.tedi.dto.InfoChangeDto;
import com.tediproject.tedi.dto.LoginDto;
import com.tediproject.tedi.dto.PasswordChangeDto;
import com.tediproject.tedi.dto.UserDto;
import com.tediproject.tedi.model.Skills;
import com.tediproject.tedi.model.UserEntity;
import com.tediproject.tedi.repo.RoleRepo;
import com.tediproject.tedi.repo.UserRepo;
import com.tediproject.tedi.security.JwtUtil;
import com.tediproject.tedi.service.UserService;


@RestController
public class UserControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping(value="/auth/")
    public ResponseEntity<?> authenticate(@RequestParam(value="token", required = false)String token) {
        try {
            jwtUtil.validateToken(token);
            return ResponseEntity.ok().body("Token is valid");
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PostMapping(value = "/SignUp/signup")
    public ResponseEntity<?> createUser(
        @RequestParam(value="email", required = false) String email,
        @RequestParam(value="firstName", required = false) String firstName,
        @RequestParam(value="lastName", required = false) String lastName,
        @RequestParam(value="password", required = false) String password,
        @RequestParam(value="phoneNumber", required = false) Long phoneNumber,
        @RequestPart(value = "profilePicture", required = false) MultipartFile pfp,
        @RequestPart(value = "resume", required = false) MultipartFile cv) {
        try {
            userService.createUser(firstName, lastName, email, password, phoneNumber, pfp, cv);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value= "/Login")
    public ResponseEntity<?> login( @RequestBody LoginDto user) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
        } 
        catch (BadCredentialsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        
        try {
            Long id = userService.loginUser(user.getEmail(), user.getPassword());
            String token = jwtUtil.generateToken(auth);
            return ResponseEntity.ok(token);
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        
        
    }
  

    @PutMapping(value= "/NewEmail")
    public ResponseEntity<?> changeEmail(@RequestBody EmailChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            Authentication auth;
            try {

                userService.changeUserEmail(change);

                auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(change.getNewEmail(), change.getPassword())
                );
                
                String token = jwtUtil.generateToken(auth);
                
                return ResponseEntity.ok(token);
            }
            catch (AuthenticationCredentialsNotFoundException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping(value= "/NewPassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            userService.changeUserPassword(change);
            return ResponseEntity.ok(HttpStatus.OK);
        } 
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/pfpchange")
    public ResponseEntity<?> changePfp(
        @RequestParam(value="profilePicture", required = false) MultipartFile image,
        @RequestParam(value ="token", required= false) String token){
        try {
            jwtUtil.validateToken(token);
            userService.changeUserPfp(token, image);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } 
        
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/educhange")
    public ResponseEntity<?> changeEdu(@RequestBody InfoChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            userService.changeUserEdu(change.getToken(), change.getInfo());
            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/workchange")
    public ResponseEntity<?> changeWork(@RequestBody InfoChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            userService.changeUserWork(change.getToken(), change.getInfo());
            return ResponseEntity.ok(HttpStatus.OK);
        } 
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/skillchange")
    public ResponseEntity<?> changeSkill(@RequestBody InfoChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            
            userService.changeUserSkills(change.getToken(), change.getInfo());
            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/edubool")
    public ResponseEntity<?> changeEduBool(@RequestBody BoolChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            userService.changeEduBool(change.getToken());
            return ResponseEntity.ok(HttpStatus.OK);
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/Profile/workbool")
    public ResponseEntity<?> changeWorkBool(@RequestBody BoolChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            userService.changeWorkBool(change.getToken());
            return ResponseEntity.ok(HttpStatus.OK);
        } 
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/skillsbool")
    public ResponseEntity<?> changeSkillsBool(@RequestBody BoolChangeDto change){
        try {
            jwtUtil.validateToken(change.getToken());
            userService.changeSkillsBool(change.getToken());
            return ResponseEntity.ok(HttpStatus.OK);
        } 
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }        
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/worktitle")
    public ResponseEntity<?> changeWorkTitle(@RequestBody InfoChangeDto change){
        try {
            if(jwtUtil.validateToken(change.getToken())){
            userService.changeWorkTitle(change.getToken(), change.getInfo());
            return ResponseEntity.ok(HttpStatus.OK);
            }
            else{
                return ResponseEntity.badRequest().body("Invalid Token");
            }
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value= "/Profile/workplace")
    public ResponseEntity<?> changeWorkplace(@RequestBody InfoChangeDto change){
        try {
            if(jwtUtil.validateToken(change.getToken())){
            userService.changeWorkplace(change.getToken(), change.getInfo());
            return ResponseEntity.ok(HttpStatus.OK);
            }
            else{
                return ResponseEntity.badRequest().body("Invalid Token");
            }
        } 
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @GetMapping(value = "/NewEmail", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@RequestParam(value="id", required = false) Long id) {
        try{
            
            UserEntity user = userService.getUserById(id);
        
        if(user != null){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("ID is required");
        }
    }

    @GetMapping(value = "/Profile", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserProfile(@RequestParam(value="token", required = false) String token) {
        try{
            jwtUtil.validateToken(token);
            UserEntity user = userRepo.findByEmail(jwtUtil.getEmailFromJWT(token));
 
            if(user != null){
                UserDto userDto = new UserDto();
                userDto.setFirstName(user.getFirstName());
                userDto.setLastName(user.getLastName());
                userDto.setEmail(user.getEmail());
                userDto.setPhoneNumber(user.getPhoneNumber());
                userDto.setResume(user.getResume());
                userDto.setWorkExperience(user.getWorkExperience());
                userDto.setEducation(user.getEducation());
                userDto.setPublicWork(user.getPublicWork());
                userDto.setPublicEducation(user.getPublicEducation());
                userDto.setPublicSkills(user.getPublicSkills());
                userDto.setWorkTitle(user.getWorkTitle());
                userDto.setWorkplace(user.getWorkplace());
                userDto.setWebsite(user.getWebsite());
                userDto.setId(user.getID());
                
                if (user.getProfilePicture() != null) {
                    String base64Image = Base64.getEncoder().encodeToString(user.getProfilePicture());
                    userDto.setProfilePicture(base64Image);
                }
                return ResponseEntity.ok(userDto);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("token is required");
        }
    }

    @GetMapping(value = "/ViewProfile")
    public Boolean checkConnection(@RequestParam(value="user_id", required = false)Long id,@RequestParam(value="token", required = false)String token) {
        return userService.checkIfConnected(id,token);
    }


    @GetMapping(value = "/Profile/GetSkills")
    public List<Skills> getUserSkills(String token) {
        return userService.findSkills(token);
    }

    @GetMapping(value = "/VisitProfile/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProfile(@PathVariable Long id) {

        try{
            if(id == null){
                return ResponseEntity.badRequest().body("id is required");
            }
            UserEntity user = userService.getUserById(id);

            if(user != null){
                UserDto userDto = new UserDto();
                userDto.setFirstName(user.getFirstName());
                userDto.setLastName(user.getLastName());
                userDto.setEmail(user.getEmail());
                userDto.setPhoneNumber(user.getPhoneNumber());
                userDto.setResume(user.getResume());
                userDto.setWorkExperience(user.getWorkExperience());
                userDto.setEducation(user.getEducation());
                userDto.setPublicWork(user.getPublicWork());
                userDto.setPublicEducation(user.getPublicEducation());
                userDto.setPublicSkills(user.getPublicSkills());
                userDto.setWorkTitle(user.getWorkTitle());
                userDto.setWorkplace(user.getWorkplace());
                userDto.setWebsite(user.getWebsite());
                
                if (user.getProfilePicture() != null) {
                    String base64Image = Base64.getEncoder().encodeToString(user.getProfilePicture());
                    userDto.setProfilePicture(base64Image);
                }
                return ResponseEntity.ok(userDto);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        
       
        catch(Exception e){
            return ResponseEntity.badRequest().body("token is required");
        }
    }


    @DeleteMapping(value = "/Profile/DeleteSkill")
    public ResponseEntity<?> removeSkill(@RequestParam(value="skill_id") Long skill_id,@RequestParam(value="token") String token) {
        try {
            userService.deleteSkill(skill_id,token);
            return ResponseEntity.ok("OK");
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body("ID is required");
        }
    }
}


