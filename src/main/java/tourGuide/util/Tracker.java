package tourguide.util;



import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tourguide.clients.TrackerClient;

import tourguide.service.UserService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Tracker extends Thread {
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private boolean stop = false;

	@Autowired
	UserService userService;
	@Autowired
	TrackerClient trackerClient;

	public Tracker(@Value("${testMode}") Boolean isTest) {
		if(isTest){
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					stopTracking();
				}
			});
			log.debug("Tracker stopping");
		}
		else{
			executorService.submit(this);
		}
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				log.debug("Tracker stopping");
				break;
			}
			List<String> users = userService.getAllUsersID();
			log.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			users.forEach(u -> trackerClient.trackUserLocation(u));
			stopWatch.stop();
			log.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				log.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}

	}
}
