/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.jbpm;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.remote.client.api.RemoteRestRuntimeEngineBuilder;
import org.kie.services.client.api.RemoteRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JBPMEndpoint extends DefaultEndpoint {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(JBPMEndpoint.class);

    private final JBPMConfiguration configuration;
    private RuntimeEngine runtimeEngine;

    public JBPMEndpoint(String uri, JBPMComponent component, JBPMConfiguration configuration) throws URISyntaxException, MalformedURLException {
        super(uri, component);
        this.configuration = configuration;
        LOGGER.trace("creating endpoint: [{}]", configuration);

        RemoteRestRuntimeEngineBuilder engineBuilder = RemoteRuntimeEngineFactory.newRestBuilder();
        if (configuration.getUserName() != null) {
            engineBuilder.addUserName(configuration.getUserName());
        }
        if (configuration.getPassword() != null) {
            engineBuilder.addPassword(configuration.getPassword());
        }
        if (configuration.getDeploymentId() != null) {
            engineBuilder.addDeploymentId(configuration.getDeploymentId());
        }
        if (configuration.getConnectionURL() != null) {
            engineBuilder.addUrl(configuration.getConnectionURL());
        }
        if (configuration.getProcessInstanceId() != null) {
            engineBuilder.addProcessInstanceId(configuration.getProcessInstanceId());
        }
        if (configuration.getTimeout() != null) {
            engineBuilder.addTimeout(configuration.getTimeout());
        }
        if (configuration.getExtraJaxbClasses() != null) {
            engineBuilder.addExtraJaxbClasses(configuration.getExtraJaxbClasses());
        }
        runtimeEngine = engineBuilder.build();
        LOGGER.trace("creating endpoint done");

    }

    public Producer createProducer() throws Exception {
        return new JBPMProducer(this, runtimeEngine);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Consumer not supported for " + getClass().getSimpleName() + " endpoint");
    }

    public boolean isSingleton() {
        return true;
    }

    public JBPMConfiguration getConfiguration() {
        return configuration;
    }
}
