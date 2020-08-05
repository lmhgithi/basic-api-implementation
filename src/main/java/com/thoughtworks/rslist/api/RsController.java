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
    private static final List<RsEvent> rsList = new LinkedList<RsEvent>() {{
        UserController.users.add(new User("Lily1", "male", 18, "a@b.com", "12345678901"));
        UserController.users.add(new User("Lily2", "female", 20, "b@b.com", "12345678902"));
        UserController.users.add(new User("Lily3", "male", 21, "c@b.com", "12345678903"));
        add(new RsEvent("第一条事件", "无", UserController.users.get(0)));
        add(new RsEvent("第二条事件", "无", UserController.users.get(1)));
        add(new RsEvent("第三条事件", "无", UserController.users.get(2)));
    }};

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
