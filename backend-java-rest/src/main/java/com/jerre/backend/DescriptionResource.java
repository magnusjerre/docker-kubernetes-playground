package com.jerre.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("description")
public class DescriptionResource {

    @Value("${storage.dir}")
    private String storageDir;

    @Value("${filename}")
    private String fileName;

    @GetMapping
    public String getDescription() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(storageDir + "/" + fileName)))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception";
        }
    }

    @PostMapping
    public void postDescription(@RequestBody String body) {
        if (!new File(storageDir).exists()) {
            new File(storageDir).mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(storageDir + "/" + fileName)))) {
            writer.write(body);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
