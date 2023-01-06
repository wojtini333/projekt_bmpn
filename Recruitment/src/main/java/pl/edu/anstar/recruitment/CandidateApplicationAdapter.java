package pl.edu.anstar.recruitment;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.client.api.response.ActivatedJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.postgresql.util.PSQLException;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

@Component
public class CandidateApplicationAdapter {

    private static Logger LOG = LoggerFactory.getLogger(CandidateApplicationAdapter.class);

    @JobWorker(type = "registerApplication")
    public Map<String, Object> registerApplication(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job registerApplication is started.");
        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variable (process variable & inputed variable): " + entry.getKey() + " : " + entry.getValue());
        }

        int points = 0;
        try {
            points = Integer.parseInt((String) job.getVariablesAsMap().get("points"));
            LOG.info("Points. {}", points, "points.");
        } catch (NumberFormatException e) {
            LOG.info("Cannot convert String to int. {}", e);
        }

        CandidateApplication candidateApplication = new CandidateApplication(
                (String) job.getVariablesAsMap().get("firstName"),
                (String) job.getVariablesAsMap().get("lastName"),
                (String) job.getVariablesAsMap().get("email"),
                points,
                (String) job.getVariablesAsMap().get("faculty"),
                (boolean) job.getVariablesAsMap().get("olympic")
                //(String) job.getVariablesAsMap().get("decision")
        );

        int applicationId = Database.addApplication(candidateApplication);
        if (applicationId > 0) {
            candidateApplication.setApplicationId(applicationId);
            LOG.info("Application registered. Application ID: {}", applicationId);
            jobResultVariables.put("applicationRegistered", true);
        } else {
            LOG.info("Application not registered.");
            jobResultVariables.put("applicationRegistered", false);
        }
        jobResultVariables.put("candidateApplication", candidateApplication);
        jobResultVariables.put("applicationId", applicationId);

        return jobResultVariables;
    }

    @JobWorker(type = "sendEmail")
    public Map<String, Object> sendEmail(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job sendEmail is started.");
        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variables (process & task input): {}", entry.getKey() + " : " + entry.getValue());
        }

        // CandidateApplication candidateApplication = (CandidateApplication) job.getVariablesAsMap().get("candidateApplication");
        // MailTemplate mt = new MailTemplate(0, candidateApplication.getFirstName() + " " + candidateApplication.getLastName(), candidateApplication.getEmail(), null, candidateApplication.getApplicationId());

        MailTemplate mt = new MailTemplate(0, (String) job.getVariablesAsMap().get("firstName") + " " + (String) job.getVariablesAsMap().get("lastName"), (String) job.getVariablesAsMap().get("email"), null, (Integer) job.getVariablesAsMap().get("applicationId"));
        try {
            mt.sendMail();
            LOG.info("Sending mail succeeded.");
            jobResultVariables.put("mailSendingResult", true);
        } catch (Exception e) {
            LOG.error("Sending mail failed.");
            jobResultVariables.put("mailSendingResult", false);
        }

        return jobResultVariables;
    }

    @JobWorker(type = "registerDecision")
    public Map<String, Object> registerDecision(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job registerDecision is started.");
        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variable (process variable & inputed variable): " + entry.getKey() + " : " + entry.getValue());
        }

        String decision = (String) job.getVariablesAsMap().get("decision");
        int applicationId = (Integer) job.getVariablesAsMap().get("applicationId");

        int countUpdatedRows = Database.updateApplicationDecision(applicationId, decision);
        if (countUpdatedRows > 0) {
            LOG.info("Decision registered. Application ID: {}", applicationId + " / " + "Decision: " + decision);
            jobResultVariables.put("decisionRegistered", true);
        } else {
            LOG.info("Decision not registered.");
            jobResultVariables.put("decisionRegistered", false);
        }

        return jobResultVariables;
    }

}