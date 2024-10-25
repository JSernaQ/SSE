package com.example.sse.services;


import ch.qos.logback.core.model.SerializeModelModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @Scheduled(fixedRate = 1000)
    public void sendEvents() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(System.currentTimeMillis());
            }catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
