package com.friday;

import com.friday.persist.Perister;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class Router {
    @Autowired
    private final Perister perister;
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());

    public Router(Perister perister) throws IOException {
        this.perister = perister;
        FileHandler fileHandler = new FileHandler("friday_router%u.log", 1024 * 1024, 5, true);
        fileHandler.setLevel(Level.ALL);
        LOGGER.addHandler(fileHandler);
        LOGGER.setLevel(Level.ALL);
    }

    @GetMapping("/")
    public String  swagger(){
        return "see available <A HREF=\"swagger-ui/index.html\">API usage</A>";
    }
    @PostMapping("/api/add")
    public ResponseEntity<UserInfo> add(@Valid @RequestBody User user) {
        int count = this.perister.add(user);
        return count > 0 ? new ResponseEntity<>(new UserInfo("User " + user.email() + " created successfully"), HttpStatus.CREATED) :
                new ResponseEntity<>(new UserInfo(user.email() + " not added"), HttpStatus.PARTIAL_CONTENT);
    }

    @PostMapping("/api/update")
    public ResponseEntity<UserInfo> update(@Valid @RequestBody User user) {
        int count = this.perister.update(user);
        return count > 0 ? new ResponseEntity<>(new UserInfo("User " + user.email() + " updated successfully"), HttpStatus.CREATED) :
                new ResponseEntity<>(new UserInfo(user.email() + " not updated"), HttpStatus.PARTIAL_CONTENT);
    }

    @PostMapping("/api/delete")
    public ResponseEntity<UserInfo> delete(@Valid @RequestBody UserLocator locator) {
        int count = this.perister.delete(locator.email());
        return count > 0 ? new ResponseEntity<>(new UserInfo("User " + locator.email() + " deleted successfully"), HttpStatus.CREATED) :
                new ResponseEntity<>(new UserInfo(locator.email() + " not deleted"), HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping("/api/getByEmail")
    public ResponseEntity<?> getByEmail(@NotBlank(message = "Email is mandatory") @Email(message = "Invalid email address") String email) {
        User user = this.perister.getByEmail(email);
        return user == null ? new ResponseEntity<>(new UserInfo(email + " not found"), HttpStatus.PARTIAL_CONTENT) :
                new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/api/getAll")
    public ResponseEntity<?> getAll() {
        List<User> users = this.perister.get();
        return users == null || users.isEmpty() ? new ResponseEntity<>(new UserInfo("No users registered"), HttpStatus.PARTIAL_CONTENT) :
                new ResponseEntity<>(users, HttpStatus.OK);
    }
}