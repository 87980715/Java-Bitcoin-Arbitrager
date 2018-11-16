package main;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.math.BigDecimal;

public class ExchangeQuote {
    private String exchangeName;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;

    private ExchangeQuote(String exchangeName, BigDecimal bidPrice, BigDecimal askPrice) {
        this.exchangeName = exchangeName;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public static ExchangeQuote getExchangeQuote(String exchangeName, String exchangeClassName) throws IOException {

        //get instance of given exchange
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exchangeClassName);

        //get market data
        MarketDataService marketDataService = exchange.getMarketDataService();

        //append snapshot of data to array
        Ticker ticker = marketDataService.getTicker(CurrencyPair.BTC_USD);

        //generate quote from ticker data
        ExchangeQuote quote = new ExchangeQuote(exchangeName, ticker.getBid(), ticker.getAsk());

        return quote;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }
}
