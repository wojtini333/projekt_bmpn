package pl.edu.anstar;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;


@Service
@Component
public class CandidateApplicationAdapter {

    private static Logger LOG = LoggerFactory.getLogger(CandidateApplicationAdapter.class);

    private final JavaMailSender javaMailSender;

    public CandidateApplicationAdapter(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //    @JobWorker(type = "registerApplication")
//    public Map<String, Object> registerApplication(final JobClient client, final ActivatedJob job) {
//        HashMap<String, Object> jobResultVariables = new HashMap<>();
//
//        LOG.info("Job registerApplication is started.");
//        final Map<String, Object> jobVariables = job.getVariablesAsMap();
//        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
//            LOG.info("Job variable (process variable & inputed variable): " + entry.getKey() + " : " + entry.getValue());
//        }
//
//        int points = 0;
//        try {
//            points = Integer.parseInt((String) job.getVariablesAsMap().get("points"));
//            LOG.info("Points. {}", points, "points.");
//        } catch (NumberFormatException e) {
//            LOG.info("Cannot convert String to int. {}", e);
//        }
//
//        CandidateApplication candidateApplication = new CandidateApplication(
//                (String) job.getVariablesAsMap().get("firstName"),
//                (String) job.getVariablesAsMap().get("lastName"),
//                (String) job.getVariablesAsMap().get("email"),
//                points,
//                (String) job.getVariablesAsMap().get("faculty"),
//                (boolean) job.getVariablesAsMap().get("olympic")
//                //(String) job.getVariablesAsMap().get("decision")
//        );
//
//        int applicationId = Database.addApplication(candidateApplication);
//        if (applicationId > 0) {
//            candidateApplication.setApplicationId(applicationId);
//            LOG.info("Application registered. Application ID: {}", applicationId);
//            jobResultVariables.put("applicationRegistered", true);
//        } else {
//            LOG.info("Application not registered.");
//            jobResultVariables.put("applicationRegistered", false);
//        }
//        jobResultVariables.put("candidateApplication", candidateApplication);
//        jobResultVariables.put("applicationId", applicationId);
//
//        return jobResultVariables;
//    }


    @JobWorker(type = "registerClient")
    public Map<String, Object> addToDatabase(final JobClient client, final ActivatedJob job1) {
        HashMap<String, Object> jobResultVariables1 = new HashMap<>();

        LOG.info("Job registerApplication is started.");
        final Map<String, Object> jobVariables1 = job1.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables1.entrySet()) {
            LOG.info("Job variable (process variable & inputed variable): " + entry.getKey() + " : " + entry.getValue());
        }
        Reservation reservation =  new Reservation(
                (String) job1.getVariablesAsMap().get("firstname"),
                (String)job1.getVariablesAsMap().get("lastname"),
                (String)job1.getVariablesAsMap().get("email"),
                (String) job1.getVariablesAsMap().get("phone"),
                (String)job1.getVariablesAsMap().get("field_0pski5r"),
                (String)job1.getVariablesAsMap().get("field_0nxeudx")

        );

        int applicationId = Database.addClients(reservation);
        if (applicationId > 0) {
            reservation.setReservation_id(applicationId);
            LOG.info("Application registered. Application ID: {}", applicationId);
            jobResultVariables1.put("YES", true);
        } else {
            LOG.info("Application not registered.");
            jobResultVariables1.put("applicationRegistered", false);
        }
        jobResultVariables1.put("candidateApplication", reservation);
        jobResultVariables1.put("applicationId", applicationId);

        return jobResultVariables1;
    }


    @JobWorker(type = "isTicketAvailable")
    public Map<String, Object> isTicketAvailable(final JobClient client, final ActivatedJob job1) {
        HashMap<String, Object> jobResultVariables1 = new HashMap<>();

        LOG.info("Job registerApplication is started.");
        final Map<String, Object> jobVariables1 = job1.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables1.entrySet()) {
            LOG.info("Job variable (process variable & inputed variable): " + entry.getKey() + " : " + entry.getValue());
        }
        jobResultVariables1.put("YES",true);

        return jobResultVariables1;
    }

    @JobWorker(type = "sendPositiveMail")
    public Map<String, Object> sendPositiveMail(final JobClient client, final ActivatedJob job1) {
        HashMap<String, Object> jobResultVariables1 = new HashMap<>();

        LOG.info("Job registerApplication is started.");
        final Map<String, Object> jobVariables1 = job1.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables1.entrySet()) {
            LOG.info("Job variable (process variable & inputed variable): " + entry.getKey() + " : " + entry.getValue());
        }

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("bme.ans.rsjk@gmail.com");
//        message.setTo("");
//        message.setSubject("");
//        message.setText("");
//        javaMailSender.send(message);

        jobResultVariables1.put("mailSent",true);

        return jobResultVariables1;
    }


    @JobWorker(type = "sendMail")
    public Map<String, Object> sendEmail(final JobClient client, final ActivatedJob job) {
        HashMap<String, Object> jobResultVariables = new HashMap<>();

        LOG.info("Job sendEmail is started.");
        final Map<String, Object> jobVariables = job.getVariablesAsMap();
        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
            LOG.info("Job variables (process & task input): {}", entry.getKey() + " : " + entry.getValue());
        }


        Reservation reservation =  new Reservation(
                (String) job.getVariablesAsMap().get("firstname"),
                (String)job.getVariablesAsMap().get("lastname"),
                (String)job.getVariablesAsMap().get("email"),
                (String) job.getVariablesAsMap().get("phone"),
                (String)job.getVariablesAsMap().get("field_0pski5r"),
                (String)job.getVariablesAsMap().get("field_0nxeudx")

        );

        MimeMessage message2Send = javaMailSender.createMimeMessage();

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("bme.ans.rsjk@gmail.com");
//        message.setTo(reservation.getEmail());
//        message.setSubject("Test xd");


        try {
            javaMailSender.send(message2Send);
            LOG.info("Sending mail succeeded.");
            jobResultVariables.put("mailSendingResult", true);
        } catch (Exception e) {
            LOG.error("Sending mail failed.");
            jobResultVariables.put("mailSendingResult", false);
        }

        return jobResultVariables;
    }



//    @JobWorker(type = "registerDecision")
//    public Map<String, Object> registerDecision(final JobClient client, final ActivatedJob job) {
//        HashMap<String, Object> jobResultVariables = new HashMap<>();
//
//        LOG.info("Job registerDecision is started.");
//        final Map<String, Object> jobVariables = job.getVariablesAsMap();
//        for (Map.Entry<String, Object> entry : jobVariables.entrySet()) {
//            LOG.info("Job variable (process variable & inputed variable): " + entry.getKey() + " : " + entry.getValue());
//        }
//
//        String decision = (String) job.getVariablesAsMap().get("decision");
//        int applicationId = (Integer) job.getVariablesAsMap().get("applicationId");
//
//        int countUpdatedRows = Database.updateApplicationDecision(applicationId, decision);
//        if (countUpdatedRows > 0) {
//            LOG.info("Decision registered. Application ID: {}", applicationId + " / " + "Decision: " + decision);
//            jobResultVariables.put("decisionRegistered", true);
//        } else {
//            LOG.info("Decision not registered.");
//            jobResultVariables.put("decisionRegistered", false);
//        }
//
//        return jobResultVariables;
//    }

}