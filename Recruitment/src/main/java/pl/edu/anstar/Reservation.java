package pl.edu.anstar;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    private String firstName;
    private String lastName;
    private String email;
    private String phone_number;
    private String type_of_trip;
    private String date;
    private String time;
    private String departure;
    private String destination;
}