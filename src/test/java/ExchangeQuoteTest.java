import main.ExchangeQuote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.bitstamp.BitstampExchange;

import java.io.IOException;
import java.math.BigDecimal;

public class ExchangeQuoteTest {
    private static boolean setUpIsDone = false;
    private static ExchangeQuote quote;

    @Before
    public void setupQuote() {
        if (ExchangeQuoteTest.setUpIsDone) {
            return;
        }

        try {
            ExchangeQuoteTest.quote = ExchangeQuote.getExchangeQuote("Bitstamp",BitstampExchange.class.getName());
            ExchangeQuoteTest.setUpIsDone = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Assert.fail("Ran into an IO Exception during the test");
        }
    }

    @Test
    public void ExchangeQuoteSmokeTest() {
        Assert.assertTrue(ExchangeQuoteTest.quote != null);
    }

    @Test
    public void getExchangeNameTest() {
        Assert.assertTrue(ExchangeQuoteTest.quote.getExchangeName() == "Bitstamp");
    }


}
