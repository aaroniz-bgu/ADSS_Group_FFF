package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.DeliveryDto;
import bgu.adss.fff.dev.contracts.MakeDeliveryRequest;
import bgu.adss.fff.dev.controllers.mappers.DeliveryMapper;
import bgu.adss.fff.dev.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService service;

    @Autowired
    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<DeliveryDto[]> getDeliveries() {
        return ResponseEntity.ok(service.getDeliveries().stream()
                .map(DeliveryMapper::map)
                .toList()
                .toArray(new DeliveryDto[0]));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDelivery(@PathVariable long id) {
        return ResponseEntity.ok(DeliveryMapper.map(service.getDelivery(id)));
    }

    @PostMapping
    public ResponseEntity<Long> registerDelivery(@RequestBody MakeDeliveryRequest request) {
        return ResponseEntity.ok(service.registerDelivery(
                request.source(),
                request.start(),
                request.truckNumber(),
                request.license(),
                Arrays.stream(request.destinations()).toList()
            ).getId()
        );
    }
}
