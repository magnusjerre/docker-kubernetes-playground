package com.jerre.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("persons")
public class PersonResource {

    @Autowired
    PersonRepository accountRepository;

    @GetMapping()
    public List<String> getPersons() {
        List<String> output = new ArrayList<>();
        for (Person person : accountRepository.findAll()) {
            output.add(person.name);
        }
        return output;
    }

}

