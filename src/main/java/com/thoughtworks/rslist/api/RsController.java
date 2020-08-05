package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;


@RestController
public class RsController {
    public static List<RsEvent> rsList = new LinkedList<>();

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getRsListBetween(@RequestParam(required = false) Integer start,
                                                          @RequestParam(required = false) Integer end) {

        if(start != null && end != null) {
            new ResponseEntity<>(rsList.subList(start - 1, end), HttpStatus.OK);
        }
        return ResponseEntity.ok(rsList);
    }

    @PostMapping("/rs/event")
    public ResponseEntity createRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);
        if (!UserController.users.contains(rsEvent.getUser())) {
            UserController.register(rsEvent.getUser());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getRsListByIndex(@PathVariable Integer index) {
        return ResponseEntity.ok(rsList.get(index));
    }

    @PostMapping("/rs/modify/{index}")
    public ResponseEntity modifyRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        rsList.get(index).setEventName(rsEvent.getEventName());
        rsList.get(index).setKeyword(rsEvent.getKeyword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rs/delete/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        rsList.remove(index);
        return ResponseEntity.ok().build();
    }
}
