package scripts.api.util.numbers;

import scripts.wastedbro.api.rsitem_services.RsItemPriceService;
import scripts.wastedbro.api.rsitem_services.grand_exchange_api.GrandExchangePriceService;
import scripts.wastedbro.api.rsitem_services.rsbuddy.RsBuddyPriceService;
import scripts.wastedbro.api.rsitem_services.runelite.RuneLitePriceService;

import java.util.Optional;

public class Prices {
    public static RsItemPriceService priceService = new RsItemPriceService.Builder()
            .addPriceService(new RuneLitePriceService())
            .addPriceService(new RsBuddyPriceService())
            .addPriceService(new GrandExchangePriceService())
            .build();

    public static Optional<Integer> getPrices(Integer ID){
       return priceService.getPrice(ID);
    }

    public static Optional<Integer> getScaledPrice(Integer id, Integer count){
        return Optional.of(Prices.getPrices(id).get() * count);
    }



}
