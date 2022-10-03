package com.calebe.reactiveexamples;

import com.calebe.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;
    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }
    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }
    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(person -> {
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println("From map: " + firstName);
        });
    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person.toString());
    }

    @Test
    void fluxTestSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void fluxTestToListMono() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> personListMono = personFlux.collectList();

        personListMono.subscribe(list -> {
           list.forEach(person -> {
               System.out.println(person.toString());
           });
        });
    }

    @Test
    void fluxTestPersonById() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer ID = 3;

        Mono<Person> personMono = personFlux.filter(person -> {
            return person.getId() == ID;
        }).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void fluxTestPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer ID = 8;

        Mono<Person> personMono = personFlux.filter(person -> {
            return person.getId() == ID;
        }).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void fluxTestPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer ID = 8;

        Mono<Person> personMono = personFlux.filter(person -> {
            return person.getId() == ID;
        }).single();

        personMono.doOnError(throwable -> {
            System.out.println("I went boom");
        }).onErrorReturn(Person.builder().id(ID).build()).subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindByIdWithImplementation() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {
            System.out.println(person);
        });
    }

    @Test
    void testFindByIdWithImplementationNotFound() {
        Mono<Person> personMono = personRepository.getById(6);

        personMono.subscribe(person -> {
            System.out.println(person);
        });
    }
}