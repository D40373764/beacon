package services;

import javax.inject.Singleton;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Singleton
public class DataStaxUtil {

	private static final Config config = ConfigFactory.load();
	private static final String cassandraHost = config.getString("datastax.url"); 
	private static final int cassandraPost= config.getInt("datastax.port");
	private static final String keyspace = config.getString("datastax.keyspace");
	private static final Cluster cluster = Cluster.builder()
			.addContactPoint(cassandraHost)
			.withPort(cassandraPost)
			.withCredentials(config.getString("datastax.username"), config.getString("datastax.password"))
			.build();

	public static Session connect() {
		return cluster.connect(keyspace);		
	}
}
