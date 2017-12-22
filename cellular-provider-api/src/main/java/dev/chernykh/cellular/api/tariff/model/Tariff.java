package dev.chernykh.cellular.api.tariff.model;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tariffs")
@Data
public class Tariff {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String tariffName;
    @Column(name = "is_active")
    private boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tariff_id")
    @MapKey(name = "optionName")
    private Map<String, Option> options = new HashMap<>();

    public static Tariff from(String tariffName, double callPrice, double smsPrice) {
        AtomicLong gen = new AtomicLong();
        Option call = new Option(
                gen.incrementAndGet(),
                "Voice call",
                Money.of(CurrencyUnit.ofNumericCode(643), callPrice),
                Money.of(CurrencyUnit.ofNumericCode(643), callPrice),
                LocalDate.now()
        );

        Option sms = new Option(
                gen.incrementAndGet(),
                "SMS",
                Money.of(CurrencyUnit.ofNumericCode(643), smsPrice),
                Money.of(CurrencyUnit.ofNumericCode(643), smsPrice),
                LocalDate.now()
        );

        return new Tariff(
                gen.incrementAndGet(),
                tariffName,
                false,
                ImmutableMap.of("call", call, "sms", sms)
        );
    }

    public static Tariff from(long id, String tariffName, double callPrice, double smsPrice) {
        AtomicLong gen = new AtomicLong();
        Option call = new Option(
                gen.incrementAndGet(),
                "Voice call",
                Money.of(CurrencyUnit.ofNumericCode(643), callPrice),
                Money.of(CurrencyUnit.ofNumericCode(643), callPrice),
                LocalDate.now()
        );

        Option sms = new Option(
                gen.incrementAndGet(),
                "SMS",
                Money.of(CurrencyUnit.ofNumericCode(643), smsPrice),
                Money.of(CurrencyUnit.ofNumericCode(643), smsPrice),
                LocalDate.now()
        );

        return new Tariff(
                id,
                tariffName,
                false,
                ImmutableMap.of("call", call, "sms", sms)
        );
    }
}
