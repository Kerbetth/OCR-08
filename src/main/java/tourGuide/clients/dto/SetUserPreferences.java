package tourGuide.clients.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SetUserPreferences {

    private int attractionProximity;
    private String currencyUnit;
    private int lowerPricePoint;
    private int highPricePoint;
    private int tripDuration;
    private int ticketQuantity;
    private int numberOfAdults;
    private int numberOfChildren;

    @JsonCreator
    public SetUserPreferences(int attractionProximity, String currencyUnit, int lowerPricePoint, int highPricePoint, int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {
        this.attractionProximity = attractionProximity;
        this.currencyUnit = currencyUnit;
        this.lowerPricePoint = lowerPricePoint;
        this.highPricePoint = highPricePoint;
        this.tripDuration = tripDuration;
        this.ticketQuantity = ticketQuantity;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }
}
