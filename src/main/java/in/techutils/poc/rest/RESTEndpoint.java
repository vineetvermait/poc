package in.techutils.poc.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTEndpoint {
    @GetMapping(value = "/{name}")
    public ResponseEntity hello(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok("Hello World, " + name);
    }
}
