package pl.edu.anstar;

import java.sql.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class Database {

//    public static void main(String[] args) {;
//        System.out.println(fetchQueuedTickets().get(0).getFirst_name());
//    }
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://195.150.230.210:5434/2021_jaklinski_wojciech";
    static final String USER = "2021_jaklinski_wojciech";
    static final String PASS = "33164";

    private static Logger LOG = LoggerFactory.getLogger(Database.class);

    public Database() {
    }

    private static void close(Connection connection, Statement stmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
                LOG.info("Object {} closed.", rs.getClass().getName());
            }
        } catch (Exception e) {
            LOG.error("An exception occurred while closing a result set.", e);
        }
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
                LOG.info("Object {} closed.", stmt.getClass().getName());
            }
        } catch (NullPointerException e) {
            LOG.error("Null pointer exception occurred while closing Statement object.", e);
        } catch (Exception e) {
            LOG.error("An exception occurred while closing a {}.", stmt.getClass().getName() + e);
        }
        try {
            if (connection != null && !connection.isClosed()) {
                if (!connection.getAutoCommit()) {
                    try {
                        connection.setAutoCommit(true);
                        LOG.info("Connection AutoCommit mode set to {}.", connection.getAutoCommit());
                    } catch (SQLException e) {
                        LOG.error("An exception occurred while setting connection AutoCommit mode.", e);
                    }
                }
                connection.close();
                LOG.info("Object {} closed.", connection.getClass().getName());
            }
        } catch (Exception e) {
            LOG.error("An exception occurred while closing a connection.", e);
        }
    }

//    public static int addApplication(CandidateApplication application) {
//        int applicationID = 0;
//        Connection connection = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//
//        try {
//            Class.forName(JDBC_DRIVER);
//
//            connection = DriverManager.getConnection(DB_URL, USER, PASS);
//            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//
//            String sql = "INSERT INTO candidate_aplication.rezerwacja VALUES (DEFAULT,?,?,?,?,?,?,?,DEFAULT) RETURNING application_id";
//
//            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            pstmt.clearParameters();
//
//            pstmt.setString(1, application.getFirstName());
//            pstmt.setString(2, application.getLastName());
//            pstmt.setString(3, application.getEmail());
//            pstmt.setInt(4, application.getPoints());
//            pstmt.setString(5, application.getFaculty());
//            pstmt.setBoolean(6, application.getOlympic());
//
//
//            rs = pstmt.executeQuery();
//            rs.beforeFirst();
//            if (rs.next()) {
//                applicationID = rs.getInt("application_id");
//                LOG.info("Row inserted. Application ID: {}.", applicationID);
//            } else {
//                LOG.info("No rows were inserted.");
//            }
//
//        } catch (ClassNotFoundException e) {
//            LOG.error("An exception occurred while loading JDBC class.", e);
//        } catch (Exception e) {
//            LOG.error("A generic exception occurred.", e);
//        } finally {
//            close(connection, pstmt, rs);
//        }
//        return applicationID;
//    }
    public static ArrayList<PendingTicketEntity> fetchQueuedTickets(){

        final String QUERY = "select * from flight_book_app.pending_tickets limit 1";
        ArrayList<PendingTicketEntity> pendingTicketEntityArrayList = new ArrayList<>();

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            stmt = connection.createStatement();
            rs = stmt.executeQuery(QUERY);

            while(rs.next()){
                //Display values
                pendingTicketEntityArrayList.add(
                        PendingTicketEntity.builder()
                                .reservation_id(rs.getInt("reservation_id"))
                                .first_name(rs.getString("first_name"))
                                .last_name(rs.getString("last_name"))
                                .e_mail(rs.getString("e_mail"))
                                .phone_number(rs.getString("phone_number"))
                                .type_of_trip(rs.getString("type_of_trip"))
                                .date(rs.getString("date"))
                                .time(rs.getString("time"))
                                .departure_id(rs.getString("departure_id"))
                                .destination_id(rs.getString("destination_id"))
                                .build()
                );
            }

        } catch (ClassNotFoundException e) {
            LOG.error("An exception occurred while loading JDBC class.", e);
        } catch (Exception e) {
            LOG.error("A generic exception occurred.", e);
        } finally {
            close(connection, stmt, rs);
        }

        return pendingTicketEntityArrayList;
    }

    public static int AddApprovedTicket(ApprovedTicketDto approvedTicketDto){

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "INSERT INTO flight_book_app.approved_tickets VALUES (DEFAULT,?,?,?,?,?,?,?,?,?) RETURNING id";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();

            LOG.info("reservation " + approvedTicketDto.getFirst_name());

            pstmt.setString(1, approvedTicketDto.getFirst_name());
            pstmt.setString(2, approvedTicketDto.getLast_name());
            pstmt.setString(3, approvedTicketDto.getE_mail());
            pstmt.setString(4, approvedTicketDto.getPhone_number());
            pstmt.setString(5, approvedTicketDto.getType_of_trip());
            pstmt.setString(6, approvedTicketDto.getDate());
            pstmt.setString(7, approvedTicketDto.getTime());
            pstmt.setString(8, approvedTicketDto.getDeparture_id());
            pstmt.setString(9, approvedTicketDto.getDestination_id());


            rs = pstmt.executeQuery();
            rs.beforeFirst();

            Database.DeleteApproved(approvedTicketDto.getId());


        } catch (ClassNotFoundException e) {
            LOG.error("An exception occurred while loading JDBC class.", e);
        } catch (Exception e) {
            LOG.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return 1;
    }

    private static void DeleteApproved(int id){
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
        ) {
            String delete_sql = "DELETE FROM flight_book_app.pending_tickets WHERE reservation_id = "+id;
            stmt.executeUpdate(delete_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Moja funckja
    public static int addClients(Reservation reservation) {


        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "INSERT INTO flight_book_app.pending_tickets VALUES (DEFAULT,?,?,?,?,?,?,?,?,?) RETURNING reservation_id";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();

            LOG.info("pending ticket " + reservation.getFirstName());

            pstmt.setString(1, reservation.getFirstName());
            pstmt.setString(2, reservation.getLastName());
            pstmt.setString(3, reservation.getEmail());
            pstmt.setString(4, reservation.getPhone_number());
            pstmt.setString(5, reservation.getType_of_trip());
            pstmt.setString(6, reservation.getDate());
            pstmt.setString(7, reservation.getTime());
            pstmt.setString(8, reservation.getDeparture());
            pstmt.setString(9, reservation.getDestination());


            rs = pstmt.executeQuery();
            rs.beforeFirst();


        } catch (ClassNotFoundException e) {
            LOG.error("An exception occurred while loading JDBC class.", e);
        } catch (Exception e) {
            LOG.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, rs);
        }
        return 1;
    }

    public static int updateApplicationDecision(int applicationId, String decision) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        int countUpdatedRows = 0;

        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String sql = "UPDATE recruitment.application SET decision = ? WHERE application_id = ?";

            pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.clearParameters();

            pstmt.setString(1, decision);
            pstmt.setInt(2, applicationId);

            countUpdatedRows = pstmt.executeUpdate();
            if (countUpdatedRows > 0) {
                LOG.info("Row updated: {}.", countUpdatedRows);
            } else {
                LOG.info("No rows were updated.");
            }

        } catch (ClassNotFoundException e) {
            LOG.error("An exception occurred while loading JDBC class.", e);
        } catch (Exception e) {
            LOG.error("A generic exception occurred.", e);
        } finally {
            close(connection, pstmt, null);
        }

        return countUpdatedRows;
    }

}