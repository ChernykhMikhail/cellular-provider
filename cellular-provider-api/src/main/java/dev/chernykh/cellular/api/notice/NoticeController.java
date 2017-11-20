package dev.chernykh.cellular.api.notice;

import dev.chernykh.cellular.api.tariff.model.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public ResponseEntity<String> getNotifications(@RequestBody Tariff tariff) {
        String notice = noticeService.generateNotice(tariff);
        return ResponseEntity.ok(notice);
    }
}
