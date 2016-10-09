package services;

import javax.inject.Singleton;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Singleton
public class CassandraUtil {

	private static final Config config = ConfigFactory.load();
	private static final String cassandraHost = config.getString("cassandra.url"); 
	private static final int cassandraPost= config.getInt("cassandra.port");
	private static final String keyspace = config.getString("cassandra.keyspace");
	private static final Cluster cluster = Cluster.builder()
			.addContactPoint(cassandraHost)
			.withPort(cassandraPost)
			.build();

	public static Session connect() {
		return cluster.connect(keyspace);		
	}
}
