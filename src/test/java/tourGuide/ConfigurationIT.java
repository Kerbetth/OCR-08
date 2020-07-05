package tourGuide;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.mockserver.integration.ClientAndServer;


@Configuration
public class ConfigurationIT {
    public static ClientAndServer mockTrackerService;
    public static ClientAndServer mockUserService;
    public static ClientAndServer mockPricerService;

    @PostConstruct
    void startServer() {
        mockTrackerService.startClientAndServer();
        mockUserService.startClientAndServer();
        mockPricerService.startClientAndServer();
    }

    @PreDestroy
    void stopServer() {
        mockTrackerService.startClientAndServer();
        mockUserService.startClientAndServer();
        mockPricerService.startClientAndServer();
    }

}
