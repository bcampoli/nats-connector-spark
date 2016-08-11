/*******************************************************************************
 * Copyright (c) 2016 Logimethods
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package com.logimethods.connector.spark.to_nats;

import java.util.HashMap;
import java.util.LinkedList;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;

public class SparkToStandardNatsConnectorPool extends SparkToNatsConnectorPool<SparkToStandardNatsConnectorPool> {

	protected static final HashMap<Integer, LinkedList<Connection>> connectionsPoolMap = new HashMap<Integer, LinkedList<Connection>>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// TODO No more static, needs to be checked on a cluster
	protected ConnectionFactory 	connectionFactory;
	
	/**
	 * 
	 */
	protected SparkToStandardNatsConnectorPool() {
		super();
	}

	/**
	 * @return
	 * @throws Exception
	 */
	protected SparkToStandardNatsConnectorImpl newSparkToNatsConnector() throws Exception {
		return new SparkToStandardNatsConnectorImpl(getNatsURL(), getProperties(), getConnectionTimeout(), getConnectionFactory(), getDefinedSubjects());
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
	protected void setConnectionFactory(ConnectionFactory factory) {
		connectionFactory = factory;
	}

	@Override
	protected void returnConnection(int hashCode, SparkToNatsConnector<?> connector) {
		synchronized(connectionsPoolMap) {
			LinkedList<Connection> connectorsPoolList = connectionsPoolMap.get(hashCode);
			if (connectorsPoolList == null) {
				connectorsPoolList = new LinkedList<Connection>();
				connectionsPoolMap.put(hashCode, connectorsPoolList);
			}
			connectorsPoolList.add(((SparkToStandardNatsConnectorImpl)connector).connection);
		}
	}

	protected static Connection getConnectionFromPool(Integer hashCode) {
		synchronized(connectionsPoolMap) {
			final LinkedList<Connection> connectionsList = connectionsPoolMap.get(hashCode);
			if (connectionsList != null) {
				return connectionsList.pollFirst();
			}
		}
		return null;
	}

	public static long poolSize() {
		int size = 0;
		for (LinkedList<Connection> poolList: connectionsPoolMap.values()){
			size += poolList.size();
		}
		return size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SparkToStandardNatsConnectorPool ["
				+ (connectionFactory != null ? "connectionFactory=" + connectionFactory + ", " : "")
				+ (properties != null ? "properties=" + properties + ", " : "")
				+ (subjects != null ? "subjects=" + subjects + ", " : "")
				+ (natsURL != null ? "natsURL=" + natsURL + ", " : "")
				+ ("connectionsPoolMap=" + connectionsPoolMap) + "]";
	}
}
