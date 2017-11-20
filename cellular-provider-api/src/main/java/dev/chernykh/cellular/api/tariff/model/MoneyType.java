package dev.chernykh.cellular.api.tariff.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.CurrencyType;
import org.hibernate.usertype.UserType;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;

public class MoneyType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{
                BigDecimalType.INSTANCE.sqlType(),
                CurrencyType.INSTANCE.sqlType()
        };
    }

    @Override
    public Class returnedClass() {
        return Money.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        assert names.length == 2;
        BigDecimal amount = (BigDecimal) BigDecimalType.INSTANCE.get(rs, names[0], session);
        CurrencyUnit currency = CurrencyUnit.of((Currency) CurrencyType.INSTANCE.get(rs, names[1], session));
        return amount == null && currency == null
                ? null
                : Money.of(currency, amount);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            BigDecimalType.INSTANCE.set(st, null, index, session);
            CurrencyType.INSTANCE.set(st, null, index + 1, session);
        } else {
            final Money money = (Money) value;
            BigDecimalType.INSTANCE.set(st, money.getAmount(), index, session);
            CurrencyType.INSTANCE.set(st, money.getCurrencyUnit().toCurrency(), index + 1, session);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
