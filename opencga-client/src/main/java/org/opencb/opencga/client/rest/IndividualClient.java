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

import org.opencb.commons.datastore.core.ObjectMap;
import org.opencb.commons.datastore.core.QueryResponse;
import org.opencb.opencga.catalog.exceptions.CatalogException;
import org.opencb.opencga.catalog.models.Individual;
import org.opencb.opencga.client.config.ClientConfiguration;

import java.io.IOException;

/**
 * Created by imedina on 24/05/16.
 */
public class IndividualClient extends AbstractParentClient<Individual> {

    private static final String INDIVIDUALS_URL = "individuals";

    protected IndividualClient(String sessionId, ClientConfiguration configuration) {
        super(sessionId, configuration);

        this.category = INDIVIDUALS_URL;
        this.clazz = Individual.class;
    }

    public QueryResponse<Individual> create(String studyId, String individualName, ObjectMap params) throws CatalogException, IOException {
        addParamsToObjectMap(params, "studyId", studyId, "name", individualName);
        return execute(INDIVIDUALS_URL, "create", params, Individual.class);
    }

    public QueryResponse<Individual> annotate(String individualId, String annotateSetName, ObjectMap params)
            throws CatalogException, IOException {
        addParamsToObjectMap(params, "annotateSetName", annotateSetName);
        return execute(INDIVIDUALS_URL, individualId, "annotate", params, Individual.class);
    }

}
