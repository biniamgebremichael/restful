package com.friday;

import com.friday.persist.Perister;
import com.friday.persist.SqliteOnFileDAO;
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
public class UserController {
    private final Perister perister;
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());
    private final FileHandler fileHandler;

    public UserController() throws IOException {
        this.fileHandler = new FileHandler("friday%u.log", 1024 * 1024, 5, true);
        this.fileHandler.setLevel(Level.ALL);
        LOGGER.addHandler(fileHandler);
        LOGGER.setLevel(Level.ALL);
        this.perister = new SqliteOnFileDAO(fileHandler);
    }

    @PostMapping("/api/add")
    public ResponseEntity<UserMessage> add(@Valid @RequestBody User user) {
        int count = this.perister.add(user);
        return count > 0 ? new ResponseEntity<>(new UserMessage("User " + user.email() + " created successfully"), HttpStatus.CREATED) :
                new ResponseEntity<>(new UserMessage(user.email() + " not added"), HttpStatus.PARTIAL_CONTENT);
    }

    @PostMapping("/api/update")
    public ResponseEntity<UserMessage> update(@Valid @RequestBody User user) {
        int count = this.perister.update(user);
        return count > 0 ? new ResponseEntity<>(new UserMessage("User " + user.email() + " updated successfully"), HttpStatus.CREATED) :
                new ResponseEntity<>(new UserMessage(user.email() + " not updated"), HttpStatus.PARTIAL_CONTENT);
    }

    @PostMapping("/api/delete")
    public ResponseEntity<UserMessage> delete(@Valid @RequestBody UserLocator locator) {
        int count = this.perister.delete(locator.email());
        return count > 0 ? new ResponseEntity<>(new UserMessage("User " + locator.email() + " deleted successfully"), HttpStatus.CREATED) :
                new ResponseEntity<>(new UserMessage(locator.email() + " not deleted"), HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping("/api/getByEmail")
    public ResponseEntity<?> getByEmail(@Valid @RequestParam UserLocator locator) {
        User user = this.perister.get(locator.email());
        return user == null ? new ResponseEntity<>(new UserMessage(locator.email() + " not found"), HttpStatus.PARTIAL_CONTENT) :
                new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/api/getAll")
    public ResponseEntity<?> getAll() {
        List<User> users = this.perister.get();
        return users == null || users.size() <= 0 ? new ResponseEntity<>(new UserMessage("No users registered"), HttpStatus.PARTIAL_CONTENT) :
                new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
}