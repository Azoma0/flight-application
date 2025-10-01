package by.azoma.dao;

import by.azoma.entity.Ticket;
import by.azoma.exciption.DaoException;
import by.azoma.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TicketDao {
    public static final TicketDao INSTANCE = new TicketDao();

    private static final String SAVE_SQL = """
            insert into ticket( passport_no, passenger_name, flight_id, seat_no, cost) 
            values (?,?,?,?,?);
            """;
    private static final String DELETE_SQL = """
            delete from ticket
            where id = ?;
            """;
    private static final String UPDATE_SQL = """
            update ticket set passport_no = ?, passenger_name = ?, flight_id = ?, seat_no = ?, cost = ?
            where id = ?;
            """;
    private static final String FIND_ALL_SQL = """
            select id, passport_no, passenger_name, flight_id, seat_no, cost from ticket;
            """;
    private static final String FIND_BY_ID_SQL = """
            select id, passport_no, passenger_name, flight_id, seat_no, cost from ticket
            where id = ?;
            """;

    public Ticket save(Ticket ticket){
        try (var connection = ConnectionManager.open(); var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, ticket.getPassportNo());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlightId());
            statement.setString(4, ticket.getSeatNo());
            statement.setBigDecimal(5, ticket.getCost());

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if(keys.next()){
                ticket.setId(keys.getLong("id"));
            }
            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id){
        try (var connection =ConnectionManager.open(); var statement = connection.prepareStatement(DELETE_SQL)){
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(Ticket ticket){
        try (var connection = ConnectionManager.open(); var statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, ticket.getPassportNo());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlightId());
            statement.setString(4, ticket.getSeatNo());
            statement.setBigDecimal(5, ticket.getCost());
            statement.setLong(6, ticket.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAll(){
        try (var connection = ConnectionManager.open(); var statement = connection.prepareStatement(FIND_ALL_SQL)){
            List<Ticket> tickets = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()){
                buildTicket(result);
            }
            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Ticket buildTicket(ResultSet result) throws SQLException {
        return new Ticket(
                result.getLong("id"),
                result.getString("passport_no"),
                result.getString("passenger_name"),
                result.getLong("flight_id"),
                result.getString("seat_no"),
                result.getBigDecimal("cost")
        );
    }

    public Ticket findById(Long id){
        try (var connection = ConnectionManager.open(); var statement = connection.prepareStatement(FIND_BY_ID_SQL)){
        statement.setLong(1, id);
        var result = statement.executeQuery();
        Ticket ticket = new Ticket();
        if(result.next()){
            ticket = buildTicket(result);
        }
        return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public static TicketDao getInstance(){
        return INSTANCE;
    }

    private TicketDao(){

    }
}
