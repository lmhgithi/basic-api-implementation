package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
public class RsController {
    public static List<RsEvent> rsList = new LinkedList<>();

    @GetMapping("/rs/list")
    public List<RsEvent> getRsListBetween(@RequestParam(required = false) Integer start,
                                          @RequestParam(required = false) Integer end) {

        if(start != null && end != null) {
            return rsList.subList(start - 1, end);
        }
        return rsList;
    }

    @PostMapping("/rs/event")
    public void createRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);
        if (!UserController.users.contains(rsEvent.getUser())) {
            UserController.register(rsEvent.getUser());
        }
    }

    @GetMapping("/rs/{index}")
    public RsEvent getRsListByIndex(@PathVariable Integer index) {
        return rsList.get(index);
    }

    @PostMapping("/rs/modify/{index}")
    public void modifyRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        rsList.get(index).setEventName(rsEvent.getEventName());
        rsList.get(index).setKeyword(rsEvent.getKeyword());
    }

    @PostMapping("/rs/delete/{index}")
    public void deleteRsEvent(@PathVariable int index) {
        rsList.remove(index);
    }
}
