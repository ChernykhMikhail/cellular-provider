package dev.chernykh.cellular.api.tariff;

import dev.chernykh.cellular.api.tariff.model.Tariff;
import dev.chernykh.cellular.api.tariff.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tariffs")
public class TariffController {

    private final TariffService tariffService;

    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping
    public List<Tariff> getTariffs() {
        return tariffService.getAllTariffs();
    }

    @GetMapping("/{tariffId}")
    public ResponseEntity getTariff(@PathVariable("tariffId") Long id) {
        return tariffService.getTariffById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TariffNotFoundException(id));
    }

    @GetMapping("/from/{from}/to/{to}")
    public List<Tariff> getTariffsByDateOfChange(@PathVariable("from") String from, @PathVariable("to") String to) {
        return tariffService.getAllTariffPlanWhereDateOfChangeBetween(from, to);
    }

    @PutMapping
    public ResponseEntity updateTariffs(@RequestBody List<Tariff> tariffs) {
        tariffService.updateTariffs(tariffs);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity saveTariff(@RequestBody Tariff tariff) {
        tariffService.saveTariff(tariff);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{tariffId}")
    public ResponseEntity deleteTariff(@PathVariable("tariffId") Long id) {
        tariffService.deleteTariff(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
