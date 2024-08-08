package com.kiyata.ubg.admission.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByApplicantId(String applicantID) {
        return applicationRepository.findByApplicantID(applicantID);
    }

    public Optional<Application> getApplicationById(String id) {
        return applicationRepository.findById(id);
    }

    public Application createApplication(Application application) {
        application.setStatus("Pending"); // Default status
        return applicationRepository.save(application);
    }

    public Optional<Application> updateApplication(String id, Application updatedApplication) {
        return applicationRepository.findById(id).map(existingApplication -> {
            existingApplication.setCourseID(updatedApplication.getCourseID());
            existingApplication.setSubmissionDate(updatedApplication.getSubmissionDate());
            existingApplication.setContent(updatedApplication.getContent());
            existingApplication.setStatus(updatedApplication.getStatus());
            return applicationRepository.save(existingApplication);
        });
    }

    public void deleteApplication(String id) {
        applicationRepository.deleteById(id);
    }
}
