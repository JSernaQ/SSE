package com.example.sse.controller;

import com.example.sse.services.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class SseController {

    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseService.addEmitter(emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Conexión establecida"));
        } catch (IOException e) {
            emitter.complete();
        }
        return emitter;
    }
}