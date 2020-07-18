package com.jerre.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class UsersResource {

    @Value("${storage.dir}")
    private String storageDir;

    @Value("${filename}")
    private String fileName;

    @GetMapping("users")
    public String[] getUsers() {
        return readFile().orElse(new String[]{});
    }

    @PostMapping("users")
    public String[] addUser(@RequestBody String username)
    {
        System.out.println("addUser called");
        String[] strings = readFile().orElse(new String[]{});
        if (Arrays.stream(strings).noneMatch(name -> name.equals(username))) {
            String[] newUsernames = append(username, strings);
            UsernameFilehandler.writeUserNames(newUsernames, fileName, storageDir);
            return newUsernames;
        } else {
            return null;
        }
    }


    private Optional<String[]> readFile() {
        return UsernameFilehandler.readUserNames(fileName, storageDir);
    }

    private String[] append(String str, String[] array) {
        String[] strings = new String[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            strings[i] = array[i];
        }
        strings[array.length] = str;
        return strings;
    }
}

