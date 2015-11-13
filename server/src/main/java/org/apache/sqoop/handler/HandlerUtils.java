/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sqoop.handler;

import org.apache.sqoop.common.SqoopException;
import org.apache.sqoop.model.MConnector;
import org.apache.sqoop.model.MJob;
import org.apache.sqoop.model.MLink;
import org.apache.sqoop.repository.Repository;
import org.apache.sqoop.repository.RepositoryManager;
import org.apache.sqoop.server.common.ServerError;

public class HandlerUtils {

  public static MJob getJobFromIdentifier(String identifier) {
    Repository repository = RepositoryManager.getInstance().getRepository();
    MJob job = repository.findJob(identifier);
    if (job == null) {
      throw new SqoopException(ServerError.SERVER_0006, "Job: " + identifier
              + " doesn't exist");
    }
    return job;
  }

  public static MLink getLinkFromLinkName(String linkName) {
    Repository repository = RepositoryManager.getInstance().getRepository();
    MLink link = repository.findLink(linkName);
    if (link == null) {
      throw new SqoopException(ServerError.SERVER_0006, "Invalid link name: " + linkName
              + " doesn't exist");
    }
    return link;
  }

  public static MLink getLinkFromLinkId(Long linkId) {
    Repository repository = RepositoryManager.getInstance().getRepository();
    MLink link = repository.findLink(linkId);
    if (link == null) {
      throw new SqoopException(ServerError.SERVER_0006, "Invalid link id: " + linkId
              + " doesn't exist");
    }
    return link;
  }

  public static long getConnectorIdFromIdentifier(String identifier) {
    long connectorId;
    Repository repository = RepositoryManager.getInstance().getRepository();
    MConnector connector = repository.findConnector(identifier);
    if (connector != null) {
      connectorId = connector.getPersistenceId();
    } else {
      try {
        connectorId = Long.parseLong(identifier);
      } catch (NumberFormatException ex) {
        // this means name nor Id existed and we want to throw a user friendly
        // message than a number format exception
        throw new SqoopException(ServerError.SERVER_0005, "Invalid connector: " + identifier
            + " requested");
      }
    }
    return connectorId;
  }

  public static String getConnectorNameFromIdentifier(String identifier) {
    Repository repository = RepositoryManager.getInstance().getRepository();
    MConnector connector = repository.findConnector(identifier);
    if (connector == null) {
      long connectorId;
      try {
        connectorId = Long.parseLong(identifier);
      } catch (NumberFormatException ex) {
        // this means name nor Id existed and we want to throw a user friendly
        // message than a number format exception
        throw new SqoopException(ServerError.SERVER_0005, "Invalid connector: " + identifier
            + " requested");
      }

      connector = repository.findConnector(connectorId);
      if (connector == null) {
        throw new SqoopException(ServerError.SERVER_0006, "Connector: " + identifier
            + " doesn't exist");
      }
    }

    return connector.getUniqueName();
  }
}
