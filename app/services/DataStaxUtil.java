package services;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Logger;
import play.inject.ApplicationLifecycle;

@Singleton
public class DataStaxUtil {
	
	private static final Config config = ConfigFactory.load();
	private static final String cassandraHost = config.getString("datastax.url"); 
	private static final int cassandraPost= config.getInt("datastax.port");
	private static final String keyspace = config.getString("datastax.keyspace");
	private static Cluster cluster;
	private static Session session;

	@Inject
	private DataStaxUtil(ApplicationLifecycle lifecycle) {
		cluster = Cluster.builder()
				.addContactPoint(cassandraHost)
				.withPort(cassandraPost)
				.withCredentials(config.getString("datastax.username"), config.getString("datastax.password"))
				.build();
		
		session = cluster.connect(keyspace);
		
		Logger.info("DataStaxUtil->DataStaxUtil session=" + session);
		
		lifecycle.addStopHook(() -> {
			session.close();
			cluster.close();
			return CompletableFuture.completedFuture(null);
		});
	}
	
	public static Session getSession() {
		Logger.info("DataStaxUtil->getSession session=" + session);
		
		return session;		
	}

}
