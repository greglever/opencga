/*
 * Copyright 2015 OpenCB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencb.opencga.client.rest;

import org.opencb.opencga.client.config.ClientConfiguration;

/**
 * Created by imedina on 04/05/16.
 */
public class OpenCGAClient {

    private UserClient userClient;

    private ClientConfiguration clientConfiguration;

    public OpenCGAClient() {
        // create a default configuration to localhost
    }

    public OpenCGAClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public UserClient getUserClient() {
        if (userClient == null) {
            userClient = new UserClient(clientConfiguration);
        }
        return userClient;
    }
}
