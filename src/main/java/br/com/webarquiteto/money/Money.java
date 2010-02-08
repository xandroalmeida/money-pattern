package br.com.webarquiteto.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * @see http://martinfowler.com/eaaCatalog/money.html
 *
 * @author Alexandro D. Almeida
 */
public class Money implements Serializable, Comparable<Money> {
    private static final long serialVersionUID = -2200813103288948221L;

    private static final int[] CENTS = new int[]{1, 10, 100, 1000};

    private long amount;
    private Currency currency;

    public Money(double amount, Currency currency) {
        this.currency = currency;
        this.amount = Math.round(amount * centsFactor());
    }

    public Money(long amount, Currency currency) {
        this.currency = currency;
        this.amount = amount * centsFactor();
    }

    public Money(double amount, String currency) {
        this(amount, Currency.getInstance(currency));
    }

    private Money() {
    }

    public Money(BigDecimal amount, Currency currency, int roundMode) {
        this.currency = currency;
        this.amount = amount.divide(BigDecimal.ONE, roundMode).multiply(new BigDecimal(centsFactor())).longValue();
    }

    public Money add(Money other) {
        assertCurrency(other);
        return newMoney(amount + other.amount);
    }

    public Money subtract(Money other) {
        assertCurrency(other);
        return newMoney(amount - other.amount);
    }

    private Money newMoney(long amount) {
        Money money = new  Money();
        money.currency = currency;
        money.amount = amount;
        return money;
    }

    private void assertCurrency(Money other) {
        if (other == null || !currency.equals(other.currency)) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal amount() {
        return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
    }

    private int centsFactor() {
            return CENTS[currency.getDefaultFractionDigits()];
    }

    public int compareTo(Money other) {
        assertCurrency(other);
        if (amount < other.amount) {
            return -1;
        } else if (amount == other.amount) {
            return 0;
        } else {
            return 1;
        }
    }

    public Money multiply(double amount) {
        return multiply(new BigDecimal(amount));
    }


    private Money multiply(BigDecimal amount) {
        return multiply(amount, BigDecimal.ROUND_HALF_EVEN);
    }

    private Money multiply(BigDecimal amount, int roundMode) {
        return new Money(amount().multiply(amount), currency, roundMode);
    }

    public Money[] allocate(int n) {
        Money minResult = newMoney(amount/n);
        Money maxResult = newMoney(minResult.amount+1);
        Money[] result = new Money[n];
        int rest = (int) amount % n;

        for (int i = 0; i < rest; i++) {
            result[i] = maxResult;
        }

        for (int i = rest; i < n; i++) {
            result[i] = minResult;
        }

        return result;
    }

    public Money[] allocate(long[] n) {
        long total = 0;
        for (int i = 0; i < n.length; i++) total += n[i];
        long rest = amount;
        Money[] result = new Money[n.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = newMoney(amount*n[i] / total);
            rest -= result[i].amount;
        }

        for (int i = 0; i < rest; i++) {
            result[i].amount++;
        }

        return result;
    }


    @Override
    public boolean equals(Object other) {
        return (other instanceof Money) && equalsMoney((Money)other);
    }

    public boolean equalsMoney(Money other) {
        return other != null && currency.equals(other.currency) && (amount == other.amount);
    }

    @Override
    public int hashCode() {
        return (int)(amount ^ (amount >>> 32));
    }

    @Override
    public String toString() {
        return currency.getSymbol() + " " + amount();
    }
}

