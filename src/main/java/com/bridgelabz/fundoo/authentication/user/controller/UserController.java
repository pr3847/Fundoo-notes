package com.bridgelabz.fundoo.authentication.user.controller;

import com.bridgelabz.fundoo.authentication.response.Response;
import com.bridgelabz.fundoo.authentication.user.dto.LoginDto;
import com.bridgelabz.fundoo.authentication.user.model.User;
import com.bridgelabz.fundoo.authentication.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("/signup")
    public Mono<ResponseEntity<Response>> registerUser(@RequestBody User user) {
        return userServiceImpl.registerUser(user)
                .map(ResponseEntity::ok);
    }


    @GetMapping("/verify/token={token}")
    public Mono<ResponseEntity<Response>>emailVerify(@PathVariable String token){
        return userServiceImpl.verifyToken(token)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/login")
    public Mono<ResponseEntity<Response>> login(@RequestBody LoginDto loginDto) {
        return userServiceImpl.loginService(loginDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Response>> getById(@PathVariable long id){
        return userServiceImpl.getById(id);
    }

    @GetMapping("all")
    public Mono<ResponseEntity<Flux<User>>> getALl(){
        return userServiceImpl.getAll()
                .collectList()
                .map(userList -> !userList.isEmpty() ?
                        new ResponseEntity<>(Flux.fromIterable(userList), HttpStatus.OK) :
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Response>> delete(@PathVariable int id) {
        return userServiceImpl.deleteUser(id)
                .map(ResponseEntity::ok);
    }

}
