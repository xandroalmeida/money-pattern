package br.com.webarquiteto.money;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.Test;
import org.junit.Assert;
public class MoneyTest {

    @Test
    public void testNewRound() {
        Money money = new Money(BigDecimal.ONE, Currency.getInstance("USD"), BigDecimal.ROUND_HALF_EVEN);
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(100,2));
        Assert.assertEquals((double)1, money.amount().doubleValue(), 0.0);

        money = new Money(new BigDecimal("123.457"), Currency.getInstance("USD"), BigDecimal.ROUND_HALF_EVEN);
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(12345,2));
        Assert.assertEquals((double)123.45, money.amount().doubleValue(), 0.0);

    }

    @Test
    public void testOneDollar() {
        Money money = new Money(1, "USD");
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(100,2));
        Assert.assertEquals((double)1, money.amount().doubleValue(), 0.0);
    }

    @Test
    public void testOneCent() {
        Money money = new Money(0.01, "USD");
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(1,2));
        Assert.assertEquals((double)0.01, money.amount().doubleValue(), 0.0);
    }

    @Test
    public void testRound() {
        Money money = new Money(0.002, "USD");
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(0,2));

        money = new Money(0.0049, "USD");
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(0,2));

        money = new Money(0.005, "USD");
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(1,2));

        money = new Money(1.997, "USD");
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(200,2));

    }

    @Test
    public void testEquals() {
        Money money = new Money(1.995, "USD");
        Assert.assertFalse(money.equalsMoney(null));
        Assert.assertFalse(money.equals((String)null));

        Assert.assertTrue(money.equalsMoney(new Money(2, "USD")));
        Assert.assertFalse(money.equalsMoney(new Money(1.99, "USD")));

        Assert.assertFalse(money.equalsMoney(new Money(2, "BRL")));
        Assert.assertFalse(money.equalsMoney(new Money(1.99, "BRL")));
    }

    @Test
    public void testAdd() {
        Money money = new Money(0.00, "USD");
        boolean fg = false;
        try {
            money.add(null);
        } catch (IllegalArgumentException e) {
            fg = true;
        }

        Assert.assertTrue(fg);
        int c = 0;
        for (int i = 0; i < 100; i++) {
            money = money.add(new Money(0.005, "USD"));
            c++;
        }
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(100,2));
    }

    @Test
    public void testSubtract() {
        Money money = new Money(100, "USD");
        boolean fg = false;
        try {
            money.add(null);
        } catch (IllegalArgumentException e) {
            fg = true;
        }

        money = money.subtract(new Money(10, "USD"));
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(9000,2));
    }

    @Test
    public void testMultiply() {
        Money money = new Money(11, "USD");
        money = money.multiply(1.56);
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(1716,2));

        money = new Money(55, "USD");
        money = money.multiply(1.5632);
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(8597,2));

        money = new Money(7, "USD");
        money = money.multiply(1/3f);
        Assert.assertEquals(money.amount(), BigDecimal.valueOf(233,2));

    }

    @Test
    public void testAllocate() {
        Money money = new Money(3141.79, "USD");
        Money[] result = money.allocate(150);

        Money conf = new Money(0, "USD");
        for (int i = 0; i < result.length; i++) {
            conf = conf.add(result[i]);
        }

        Assert.assertEquals(conf, money);

    }
    @Test
    public void testAllocate2() {
        long [] allocations = new long[]{3,7};
        Money money = new Money(0.05, "USD");
        Money[] result = money.allocate(allocations);

        Assert.assertEquals(new Money(0.02, "USD"), result[0]);
        Assert.assertEquals(new Money(0.03, "USD"), result[1]);

    }

}
