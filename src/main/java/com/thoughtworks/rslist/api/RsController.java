package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import javassist.tools.web.BadHttpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import sun.misc.InvalidJarIndexException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.Exception;

import java.util.LinkedList;
import java.util.List;


@RestController
public class RsController {
    public static List<RsEvent> rsList = new LinkedList<>();

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getRsListBetween(@RequestParam(required = false) Integer start,
                                                          @RequestParam(required = false) Integer end) {
        if (start != null && end != null) {
            if (start < 0 || end > rsList.size()) {
                throw new InvalidJarIndexException("invalid request param");
            }
            return ResponseEntity.ok(rsList.subList(start - 1, end));
        }


        return ResponseEntity.ok(rsList);
    }

    @PostMapping("/rs/event")
    public ResponseEntity createRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);
        if (!UserController.users.contains(rsEvent.getUser())) {
            UserController.register(rsEvent.getUser());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(rsList.size() - 1));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getRsListByIndex(@PathVariable Integer index) {
        if (index < 0 || index > rsList.size()) {
            throw new InvalidJarIndexException("invalid index");
        }
        return ResponseEntity.ok(rsList.get(index));
    }

    @PostMapping("/rs/modify/{index}")
    public ResponseEntity modifyRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        rsList.get(index).setEventName(rsEvent.getEventName());
        rsList.get(index).setKeyword(rsEvent.getKeyword());

        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(index));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PostMapping("/rs/delete/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        rsList.remove(index);
        HttpHeaders headers = new HttpHeaders();
        headers.set("index", String.valueOf(index));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
