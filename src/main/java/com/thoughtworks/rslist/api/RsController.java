package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//import static org.hamcrest.Matchers.is;

@RestController
public class RsController {
    private final List<String> rsList = Stream.of("第一条事件", "第二条事件", "第三条事件").collect(Collectors.toList());

    @GetMapping("/rs/list")
    public String getRsListBetween(@RequestParam(required = false) Integer start,
                                   @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return rsList.toString();
        }
        return rsList.subList(start - 1, end).toString();
    }

    @PostMapping("/rs/event")
    public void createRsEvent(@RequestBody String rsEvent) {
        rsList.add(rsEvent);
    }

    @GetMapping("/rs/{index}")
    public String getRsListByIndex(@PathVariable Integer index) {
        return rsList.get(index);
    }

    @PostMapping("/rs/modify/{index}")
    public void modifyRsEvent(@PathVariable Integer index, @RequestBody String rsEvent) {
        rsList.set(index, rsEvent);
    }

    @PostMapping("/rs/delete/{index}")
    public void deleteRsEvent(@PathVariable int index) {
        rsList.remove(index);
    }
}
