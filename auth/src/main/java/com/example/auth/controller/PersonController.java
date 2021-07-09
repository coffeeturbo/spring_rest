package com.example.auth.controller;

import com.example.auth.domain.Person;
import com.example.auth.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;


    @PostMapping(value = {"", "/"})
    public ResponseEntity<Person> create(@RequestBody Person person) {

        var createdPerson = personService.create(person);

        return new ResponseEntity<Person>(
                createdPerson,
                HttpStatus.CREATED
        );
    }

    @PutMapping(value = {"", "/"})
    public ResponseEntity<Void> update(@RequestBody Person person) {
        personService.update(person);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = {"", "/"})
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {

        var person = personService.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }
}
