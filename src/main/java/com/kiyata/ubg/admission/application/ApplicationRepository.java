package com.kiyata.ubg.admission.application;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByApplicantID(String applicantID);
}
