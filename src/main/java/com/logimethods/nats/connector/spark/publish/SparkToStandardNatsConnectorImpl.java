/*******************************************************************************
 * Copyright (c) 2016 Logimethods
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package com.logimethods.nats.connector.spark.publish;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.apache.spark.api.java.function.VoidFunction;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;

/**
 * @author laugimethods
 *
 */
public class SparkToStandardNatsConnectorImpl extends SparkToNatsConnector<SparkToStandardNatsConnectorImpl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected transient ConnectionFactory connectionFactory = null;
	protected transient Connection connection = null;

	/**
	 * 
	 */
	public SparkToStandardNatsConnectorImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param properties
	 * @param subjects
	 * @param connectionFactory
	 */
	public SparkToStandardNatsConnectorImpl(Properties properties, Collection<String> subjects,
			ConnectionFactory connectionFactory) {
		super(properties);
		this.subjects = subjects;
		this.connectionFactory = connectionFactory;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param properties
	 * @param subjects
	 */
	public SparkToStandardNatsConnectorImpl(Properties properties, String... subjects) {
		super(properties, subjects);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param properties
	 */
	public SparkToStandardNatsConnectorImpl(Properties properties) {
		super(properties);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param subjects
	 */
	public SparkToStandardNatsConnectorImpl(String... subjects) {
		super(subjects);
		// TODO Auto-generated constructor stub
	}

	public synchronized void closeConnection() {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	/**
	 * A method that will publish the provided String into NATS through the defined subjects.
	 * @param obj the object from which the toString() will be published to NATS
	 * @throws Exception is thrown when there is no Connection nor Subject defined.
	 */
	public VoidFunction<String> publishToNats() throws Exception {
		return publishToNats;
	}

	/**
	 * A method that will publish the provided String into NATS through the defined subjects.
	 * @param obj the String that will be published to NATS.
	 * @throws Exception is thrown when there is no Connection nor Subject defined.
	 */
	protected void publishToStr(String str) throws Exception {
		if (CLOSE_CONNECTION.equals(str)) {
			closeConnection();
			return;
		}
		
		final Message natsMessage = new Message();
	
		final byte[] payload = str.getBytes();
		natsMessage.setData(payload, 0, payload.length);
	
		final Connection localConnection = getConnection();
		for (String subject : getDefinedSubjects()) {
			natsMessage.setSubject(subject);
			localConnection.publish(natsMessage);
	
			logger.trace("Send '{}' from Spark to NATS ({})", str, subject);
		}
	}

	protected synchronized Connection getConnection() throws Exception {
		if (connection == null) {
			connection = createConnection();
			getLogger().debug("A NATS Connection {} has been created for {}", connection, this);
		}
		return connection;
	}

	protected ConnectionFactory getConnectionFactory() throws Exception {
		if (connectionFactory == null) {
			connectionFactory = new ConnectionFactory(getDefinedProperties());
		}		
		return connectionFactory;
	}
	
	protected Connection createConnection() throws IOException, TimeoutException, Exception {
		return getConnectionFactory().createConnection();
	}

}