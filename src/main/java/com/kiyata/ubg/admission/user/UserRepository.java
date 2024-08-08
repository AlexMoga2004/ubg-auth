package com.kiyata.ubg.admission.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query(value = "{ '$or': [ { '$expr': { '$regexMatch': { 'input': { '$concat': ['$firstname', ' ', '$lastname'] }, 'regex': ?0, 'options': 'i' } } } ] }",
            fields = "{ 'password': 0 }")
    List<User> findByFullName(String searchTerm);

}
