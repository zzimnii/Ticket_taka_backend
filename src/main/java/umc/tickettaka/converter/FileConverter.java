package umc.tickettaka.converter;

import java.util.List;
import umc.tickettaka.domain.ticket.File;
import umc.tickettaka.domain.ticket.Ticket;

public class FileConverter {

    public static List<File> toFileList(List<String> urlList, Ticket ticket) {
        return urlList.stream()
            .map(url -> File.builder()
                .ticket(ticket)
                .url(url)
                .build()).toList();
    }
}
