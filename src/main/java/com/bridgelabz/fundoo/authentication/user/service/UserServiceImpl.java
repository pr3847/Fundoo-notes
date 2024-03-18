package com.bridgelabz.fundoo.authentication.user.service;

import com.bridgelabz.fundoo.authentication.exception.LoginException;
import com.bridgelabz.fundoo.authentication.exception.RegistrationException;
import com.bridgelabz.fundoo.authentication.response.Response;
import com.bridgelabz.fundoo.authentication.user.configuration.UserToken;
import com.bridgelabz.fundoo.authentication.user.dto.LoginDto;
import com.bridgelabz.fundoo.authentication.user.model.Email;
import com.bridgelabz.fundoo.authentication.user.model.User;
import com.bridgelabz.fundoo.authentication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    public Mono<Response> registerUser(User user) {
        return userRepository.existsByEmailId(user.getEmailId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RegistrationException("User already exists", 400));
                    } else {
                        return userRepository.save(user)
                                .doOnSuccess(user1 -> {
                                    String body="Hi "+user1.getUserName()+" welcome";
                                    sendEmail(user1.getEmailId(), body);
                                })
                                .then(Mono.just(new Response<>(200,"user registered",user)));
                    }
                });
    }
    public void sendEmail(String userEmail,String body) {
        Email email = new Email();
        email.setFrom("manoharreddyndl988@gmail.com");
        email.setTo(userEmail);
        email.setSubject("Registration");
        email.setBody(body);
        emailService.sendMail(email);
    }


    public Mono<Response> verifyToken(String token) {
        long id=UserToken.verifyToken(token);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> {
                    user.setVerified(true);
                    return userRepository.save(user)
                            .then(Mono.just(new Response<>(200,"token sucess",id)));
                });
    }


    public Mono<Response> loginService(LoginDto loginDto) {
        return userRepository.findByEmailId(loginDto.getEmail())
                .flatMap(user -> {
                    if (user.getPassword().equals(loginDto.getPassword())) {
                        String token=UserToken.generateToken((int)user.getUserId());
                        return Mono.just(new Response(200,"status.login.success",token));
                    } else {
                        return Mono.error(new LoginException("status.login.passwordNotFound", 400));
                    }
                })
                .switchIfEmpty(Mono.error(new LoginException("status.login.userNotFound", 400)));
    }


    public Mono<ResponseEntity<Response>>getById(long id){
        return userRepository.findById(id)
                .map(user -> new ResponseEntity<>(new Response(200, "Note retrieved successfully", user), HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(new Response(404, "Note not found"), HttpStatus.NOT_FOUND)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new Response(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)));

    }

    public Flux<User> getAll(){
        return userRepository.findAll();
    }


    public Mono<Response> deleteUser(long id) {
        return userRepository.existsById(id)
                .flatMap(exist->{
                    if(exist){
                        return userRepository.deleteById(id)
                                .then(Mono.just(new Response(200,"user deleted successfully")));
                    }else{
                        return Mono.just(new Response(400,"user not found"));
                    }
                });
    }


}
