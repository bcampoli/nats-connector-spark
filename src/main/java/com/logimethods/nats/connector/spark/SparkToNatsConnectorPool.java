/*******************************************************************************
 * Copyright (c) 2016 Logimethods
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package com.logimethods.nats.connector.spark;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;

public class SparkToNatsConnectorPool extends AbstractSparkToNatsConnector implements Serializable {
	
	protected Properties				properties		  = null;
	protected Collection<String>		subjects;
	protected static ConnectionFactory 	connectionFactory = null;
	protected static LinkedList<SparkToNatsConnector> connectorsPool = new LinkedList<SparkToNatsConnector>();

	static final Logger logger = LoggerFactory.getLogger(SparkToNatsConnectorPool.class);

	/**
	 * @param properties
	 * @param subjects
	 */
	protected SparkToNatsConnectorPool() {
		super();
		logger.debug("CREATE SparkToNatsConnector: " + this);
	}

	protected SparkToNatsConnectorPool(Properties properties, String... subjects) {
		super();
		this.properties = properties;
		this.subjects = Utilities.transformIntoAList(subjects);
		logger.debug("CREATE SparkToNatsConnector {} with Properties '{}' and NATS Subjects '{}'.", this, properties, subjects);
	}

	/**
	 * @param properties
	 */
	protected SparkToNatsConnectorPool(Properties properties) {
		super();
		this.properties = properties;
		logger.debug("CREATE SparkToNatsConnector {} with Properties '{}'.", this, properties);
	}

	/**
	 * @param subjects
	 */
	protected SparkToNatsConnectorPool(String... subjects) {
		super();
		this.subjects = Utilities.transformIntoAList(subjects);
		logger.debug("CREATE SparkToNatsConnector {} with NATS Subjects '{}'.", this, subjects);
	}

	public AbstractSparkToNatsConnector getConnector() throws Exception {
		synchronized(connectorsPool) {
			if (connectorsPool.size() > 0) {
				return connectorsPool.pollFirst();
			}
		}
		
		return new SparkToNatsConnector(getDefinedProperties(), getDefinedSubjects(), getDefinedConnectionFactory());
	}
	
	public void returnConnector(AbstractSparkToNatsConnector connector) {
		
	}

	/**
	 * @return the properties
	 */
	protected Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	protected void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the subjects
	 */
	protected Collection<String> getSubjects() {
		return subjects;
	}

	/**
	 * @param subjects the subjects to set
	 */
	protected void setSubjects(Collection<String> subjects) {
		this.subjects = subjects;
	}

	/**
	 * @return the connectionFactory
	 */
	protected ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	/**
	 * @param connectionFactory the connectionFactory to set
	 */
	protected void setConnectionFactory(ConnectionFactory connectionFactory) {
		SparkToNatsConnectorPool.connectionFactory = connectionFactory;
	}

	/**
	 * @return the logger
	 */
	protected Logger getLogger() {
		return logger;
	}
}
