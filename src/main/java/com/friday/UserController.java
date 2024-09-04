package com.friday;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {

    private final AtomicLong counter = new AtomicLong();
//    curl -Body "prefix=a&firstName=a&middleName=a&lastName=a&suffix=a&email=a&phone=a"  -Method POST http://localhost:8080/add

    @PostMapping("/add")
    public User add(@RequestParam(value = "prefix" ) String prefix,
                     @RequestParam(value = "firstName" ) String firstName,
                     @RequestParam(value = "middleName" ) String middleName,
                     @RequestParam(value = "lastName" ) String lastName,
                     @RequestParam(value = "suffix" )String suffix,
                    @RequestParam(value = "email" )String email,
                    @RequestParam(value = "phone" )String phone) {
        return new User(counter.incrementAndGet(), prefix,firstName,middleName ,lastName, suffix,email,phone);
    }
}