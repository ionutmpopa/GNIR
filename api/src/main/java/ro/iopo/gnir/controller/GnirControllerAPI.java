package ro.iopo.gnir.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Validated
@RequestMapping("/api/v1/gnir")
public interface GnirControllerAPI {

    @GetMapping("/test")
    ResponseEntity<String> testFunctionality() throws IOException;

    @GetMapping("/img")
    ResponseEntity<String> parseImage() throws IOException;
}
