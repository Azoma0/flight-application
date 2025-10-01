package by.azoma;


import by.azoma.dao.TicketDao;
import by.azoma.entity.Ticket;

import java.math.BigDecimal;

public class TicketRunner {
    public static void main(String[] args) {
      var ticketDao = TicketDao.getInstance();
      Ticket ticket = new Ticket();
      ticket.setId(5L);
      ticket.setPassportNo("aef");
      ticket.setPassengerName("efe");
      ticket.setFlightId(2L);
      ticket.setSeatNo("2B");
      ticket.setCost(BigDecimal.valueOf(1242));

        System.out.println(ticketDao.findById(1L));
    }
}
