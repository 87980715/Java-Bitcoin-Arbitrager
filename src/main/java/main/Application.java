package main;



import org.knowm.xchange.bitmex.BitmexExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) throws IOException {

        Map<String, String> markets = new HashMap<String, String>();

        //insert some markets
        markets.put("Bitstamp", BitstampExchange.class.getName());
        markets.put("Kraken", KrakenExchange.class.getName());
        markets.put("Bitmex", BitmexExchange.class.getName());
        markets.put("Bittrex", BittrexExchange.class.getName());

        //use array list instead of static array because some quote requests will probably fail
        ArrayList<ExchangeQuote> quotes = new ArrayList<ExchangeQuote>();

        //Get market quotes
        for (Map.Entry<String, String> market : markets.entrySet()) {
            ExchangeQuote quote = ExchangeQuote.getExchangeQuote(market.getKey(), market.getValue());

            //skip exchanges that are down or some of their services are unavailable
            if (quote == null || quote.getAskPrice() == null || quote.getBidPrice() == null) {
                continue;
            }

            quotes.add(quote);
        }

        ArrayList<String[]> arbitrageOpportunities = getArbitrageOpportunities(quotes);

        //put some space between output and debug logs
        System.out.println("\n\n\n");

        printOpportunities(arbitrageOpportunities);

    }

    public static ArrayList<String[]> getArbitrageOpportunities(ArrayList<ExchangeQuote> quotes) {
        ArrayList<String[]> arbitrageOpportunities = new ArrayList<String[]>();

        //search data for arbitrage opportunities
        for (int i = 0; i < quotes.size(); i++) {
            ExchangeQuote quoteToBuyFrom = quotes.get(i);
            for (int j = 0; j < quotes.size(); j++) {
                //no point in buying and selling to same exchange since we will always lose money on that.
                if (j == i) {
                    continue;
                }

                //Calculate the prospective profit from arbitraging the two exchanges
                ExchangeQuote quoteToSellTo = quotes.get(j);
                BigDecimal profit = quoteToSellTo.getBidPrice().subtract(quoteToBuyFrom.getAskPrice());

                //Add opportunity to list of arbitrage opportunities if profit is positive
                if (profit.signum() != -1) {
                    String[] arbitrageOpportunity = {
                            quoteToBuyFrom.getExchangeName(),
                            quoteToBuyFrom.getAskPrice().stripTrailingZeros().toString(),
                            quoteToSellTo.getExchangeName(),
                            quoteToSellTo.getBidPrice().stripTrailingZeros().toString(),
                            profit.stripTrailingZeros().toString()
                    };

                    arbitrageOpportunities.add(arbitrageOpportunity);
                }
            }
        }

        return arbitrageOpportunities;
    }

    public static void printOpportunities(ArrayList<String[]> arbitrageOpportunities) {

        if (arbitrageOpportunities.size() == 0) {
            System.out.println("There are currently no arbitrage opportunities :(");
            return;
        }

        // Print the list objects in tabular format.
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%10s %10s %10s %10s %10s \n", "BUY FROM", "BUY PRICE", "SELL TO", "SELL PRICE", "PROFIT");
        System.out.println("-----------------------------------------------------------------------------");
        for(String[] opportunity: arbitrageOpportunities){
            System.out.format("%10s %10s %10s %10s %10s",
                    opportunity[0], opportunity[1], opportunity[2], opportunity[3], opportunity[4]);
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

}
