package dev.chernykh.cellular.api.tariff;

import dev.chernykh.cellular.api.tariff.model.Tariff;
import dev.chernykh.cellular.api.tariff.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * The controller to manage tariffs.
 */
@RestController
@RequestMapping("/tariffs")
public class TariffController {

    private final TariffService tariffService;

    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    /**
     * Activate a tariff with the given id.
     *
     * @param name the tariff name
     * @return {@code HttpStatus.OK} if tariff was activated otherwise {@code HttpStatus.NOT_FOUND}
     */
    @GetMapping(value = "/activate", params = {"tariffName"})
    public ResponseEntity activateTariff(@RequestParam("tariffName") String name) {
        boolean activated = tariffService.activate(name);
        return activated ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * Deactivate a tariff with the given id.
     * @param name the tariff name
     * @return {@code HttpStatus.OK} if tariff was deactivated otherwise {@code HttpStatus.NOT_FOUND}
     */
    @GetMapping(value = "/deactivate", params = {"tariffName"})
    public ResponseEntity deactivateTariff(@RequestParam("tariffName") String name) {
        boolean deactivated = tariffService.deactivate(name);
        return deactivated ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * Return all existing tariffs.
     *
     * @return the list from tariffs
     */
    @GetMapping
    public List<Tariff> getTariffs() {
        return tariffService.getTariffs();
    }

    /**
     * Get a tariff id and return a tariff with this id if exists.
     *
     * @param id the tariff id
     * @return the found tariff
     * @throws {@link TariffNotFoundException} if tariff doesn't exists
     */
    @GetMapping("/{tariffId}")
    public ResponseEntity getTariff(@PathVariable("tariffId") Long id) {
        return tariffService.getTariffById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TariffNotFoundException(id));
    }

    /**
     * Return changed tariffs within period.
     *
     * @param from a day from the beginning period
     * @param to   a day from the end period
     * @return the list from changed tariffs
     */
    @GetMapping(params = {"from", "to"})
    public List<Tariff> getTariffsByDateOfChange(
            @RequestParam("from") String from,
            @RequestParam("to") String to) {
        return tariffService.getTariffsChangedWithinPeriod(from, to);
    }

    /**
     * Get a tariff details to update one.
     *
     * @param name      the tariff name
     * @param callPrice a price from call
     * @param smsPrice  a price from sms
     * @return {@code HttpStatus.OK}
     */
    @PostMapping(params = {"tariffName", "callPrice", "smsPrice"})
    public ResponseEntity updateTariffs(
            @RequestParam("tariffName") String name,
            @RequestParam("callPrice") BigDecimal callPrice,
            @RequestParam("smsPrice") BigDecimal smsPrice) {
        Tariff savedTariff = tariffService.createTariff(name, callPrice, smsPrice);
        return ResponseEntity.ok(savedTariff);
    }

    /**
     * Get a tariff id to delete the tariff with this id.
     *
     * @param id the tariff id
     * @return {@code HttpStatus.NO_CONTENT}
     */
    @DeleteMapping("/{tariffId}")
    public ResponseEntity deleteTariff(@PathVariable("tariffId") Long id) {
        tariffService.deleteTariff(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
