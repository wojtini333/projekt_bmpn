package pl.edu.anstar;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovedTicketDto {
    private int id;
    private String first_name;
    private String last_name;
    private String e_mail;
    private String phone_number;
    private String type_of_trip;
    private String date;
    private String time;
    private int departure_id;
    private int destination_id;
}
