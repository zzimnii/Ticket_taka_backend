package umc.tickettaka.converter;

import umc.tickettaka.domain.ticket.File;
import umc.tickettaka.domain.ticket.Ticket;

public class FileConverter {

    public static File toFile(String url, Ticket ticket) {
        return File.builder()
            .ticket(ticket)
            .url(url)
            .build();
    }
}
