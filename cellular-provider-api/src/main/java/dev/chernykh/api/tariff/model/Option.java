package dev.chernykh.api.tariff.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.chernykh.api.tariff.service.MoneyDeserializer;
import dev.chernykh.api.tariff.service.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
public class Option {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String optionName;

    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    @Type(type = "dev.chernykh.api.tariff.model.MoneyType")
    @Columns(columns = {
            @Column(name = "old_amount"),
            @Column(name = "old_currency")
    })
    private Money oldPrice;

    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    @Type(type = "dev.chernykh.api.tariff.model.MoneyType")
    @Columns(columns = {
            @Column(name = "new_amount"),
            @Column(name = "new_currency")
    })
    private Money newPrice;

    @Column(name = "date_of_change")
    private LocalDate dateOfChange;
}
