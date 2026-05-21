package com.example.billsplit.service;

import com.example.billsplit.model.Person;
import com.example.billsplit.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    // Get all persons
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // Add a person
    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    // Delete a person
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }
}
