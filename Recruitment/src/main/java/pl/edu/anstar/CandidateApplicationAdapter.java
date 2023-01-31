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
import javax.xml.crypto.Data;
import java.util.ArrayList;
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

    @JobWorker(type = "requestClientRegistration")
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
        if( applicationId == 1 )
            jobResultVariables1.put("decision",true);
        else
            jobResultVariables1.put("decision",false);

        return jobResultVariables1;
    }


    @JobWorker(type = "registerClient")
    public Map<String, Object> isTicketAvailable(final JobClient client, final ActivatedJob job1) {
        HashMap<String, Object> jobResultVariables1 = new HashMap<>();

        LOG.info("Queue is being check for pending tickets to register");
        int result;
        // My code here
        ArrayList<PendingTicketEntity> pendingTickets = Database.fetchQueuedTickets();
        if( pendingTickets.size() == 0 ){
            LOG.info("Queue is empty");
            jobResultVariables1.put("register",1);
            return jobResultVariables1;
        } else {
            PendingTicketEntity ticket =  pendingTickets.get(0);
            result = Database.AddApprovedTicket(
                    ApprovedTicketDto.builder()
                            .id(ticket.getReservation_id())
                            .first_name(ticket.getFirst_name())
                            .last_name(ticket.getLast_name())
                            .e_mail(ticket.getE_mail())
                            .phone_number(ticket.getPhone_number())
                            .type_of_trip(ticket.getType_of_trip())
                            .date(ticket.getDate())
                            .time(ticket.getTime())
                            .departure_id(ticket.getDeparture_id())
                            .destination_id(ticket.getDestination_id())
                            .build()
            );

            if( result == 1 ) {
                LOG.info("registration approved");
                jobResultVariables1.put("register", 2);
            } else {
                LOG.info("registration not approved");
                jobResultVariables1.put("register", 3);
            }

        }

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

}