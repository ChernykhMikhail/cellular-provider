package dev.chernykh.cellular.api.tariff.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyDeserializer extends StdDeserializer<Money> {

    public MoneyDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode moneyTree = p.readValueAsTree();

        BigDecimal amount = moneyTree.get("amount").decimalValue();

        JsonNode currencyNode = moneyTree.get("currency");
        CurrencyUnit currency = currencyNode == null ? CurrencyUnit.ofNumericCode(643) : CurrencyUnit.of(currencyNode.asText());

        return Money.of(currency, amount);
    }
}
