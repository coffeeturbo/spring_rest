package com.example.auth.service;

import com.example.auth.domain.Person;
import com.example.auth.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    @Autowired
    private PersonRepository persons;

    public Person create(Person person) {
        return persons.save(person);
    }

    public void update(Person person) {
        persons.save(person);
    }

    public void delete(int id) {
        Person person = new Person();
        person.setId(id);
        this.persons.delete(person);
    }

    public List<Person> findAll() {
        return StreamSupport.stream(
                this.persons.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Optional<Person> findById(int id) {
        return persons.findById(id);
    }

}
