/*
 * Copyright 2015-2017 OpenCB
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

package org.opencb.opencga.storage.core.manager.variant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.metadata.Aggregation;
import org.opencb.commons.datastore.core.Query;
import org.opencb.commons.datastore.core.QueryOptions;
import org.opencb.commons.datastore.core.QueryResult;
import org.opencb.opencga.catalog.exceptions.CatalogAuthorizationException;
import org.opencb.opencga.core.models.GroupParams;
import org.opencb.opencga.core.models.Sample;
import org.opencb.opencga.storage.core.variant.adaptors.VariantField;
import org.opencb.opencga.storage.core.variant.adaptors.VariantQueryParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created on 16/12/16.
 *
 * @author Jacobo Coll &lt;jacobo167@gmail.com&gt;
 */
public class VariantManagerFetchTest extends AbstractVariantStorageOperationTest {

    @Before
    public void setUp() throws Exception {
        indexFile(getSmallFile(), new QueryOptions(), outputId);

    }

    @Override
    protected Aggregation getAggregation() {
        return Aggregation.NONE;
    }

    @Test
    public void testCount() throws Exception {
        Query query = new Query(VariantQueryParam.STUDY.key(), studyId)
                .append(VariantQueryParam.INCLUDE_SAMPLE.key(), Collections.singletonList("NA19600"));
        variantManager.count(query, sessionId);
    }

    @Test
    public void testQuery() throws Exception {
        Query query = new Query(VariantQueryParam.STUDY.key(), studyId);
        QueryResult<Variant> result = variantManager.get(query, new QueryOptions(), sessionId);
        System.out.println("result.getNumResults() = " + result.getNumResults());
        System.out.println("result.getResult().size() = " + result.getResult().size());
        for (Variant variant : result.getResult()) {
            System.out.println("variant = " + variant);
        }
    }

    @Test
    public void testQueryProject() throws Exception {
        Query query = new Query(VariantCatalogQueryUtils.PROJECT.key(), projectId);
        QueryResult<Variant> result = variantManager.get(query, new QueryOptions(), sessionId);
        System.out.println("result.getNumResults() = " + result.getNumResults());
        System.out.println("result.getResult().size() = " + result.getResult().size());
        for (Variant variant : result.getResult()) {
            System.out.println("variant = " + variant);
        }
    }

    @Test
    public void testQueryExcludeSamples() throws Exception {
        QueryOptions queryOptions = new QueryOptions(QueryOptions.EXCLUDE, VariantField.STUDIES_SAMPLES_DATA);
        Query query = new Query();

        // Without studies
        Map<String, List<Sample>> longListMap = variantManager.checkSamplesPermissions(query, queryOptions, mockVariantDBAdaptor().getStudyConfigurationManager(), sessionId);
        Assert.assertEquals(Collections.singletonMap(studyFqn, Collections.emptyList()), longListMap);

        // With studies
        query.append(VariantQueryParam.STUDY.key(), studyFqn);
        longListMap = variantManager.checkSamplesPermissions(query, queryOptions, mockVariantDBAdaptor().getStudyConfigurationManager(), sessionId);
        Assert.assertEquals(Collections.singletonMap(studyFqn, Collections.emptyList()), longListMap);
    }

    @Test
    public void testQueryExcludeStudies() throws Exception {
        Query query = new Query(VariantQueryParam.STUDY.key(), studyId);
        QueryOptions queryOptions = new QueryOptions(QueryOptions.EXCLUDE, VariantField.STUDIES);

        Map<String, List<Sample>> longListMap = variantManager.checkSamplesPermissions(query, queryOptions, mockVariantDBAdaptor().getStudyConfigurationManager(), sessionId);
        Assert.assertEquals(Collections.emptyMap(), longListMap);
    }

    @Test
    public void testQueryAnonymous() throws Exception {
        catalogManager.getStudyManager().updateGroup(studyFqn, "@members", new GroupParams("*", GroupParams.Action.ADD), sessionId);

//        Query query = new Query(VariantDBAdaptor.VariantQueryParams.STUDIES.key(), "s1");
//        Query query = new Query(VariantDBAdaptor.VariantQueryParams.STUDIES.key(), "p1:s1");
        Query query = new Query(VariantQueryParam.STUDY.key(), userId + "@p1:s1");
//        Query query = new Query(VariantDBAdaptor.VariantQueryParams.STUDIES.key(), studyId);
        QueryResult<Variant> result = variantManager.get(query, new QueryOptions(), null);
        System.out.println("result.getNumResults() = " + result.getNumResults());
        System.out.println("result.getResult().size() = " + result.getResult().size());
        for (Variant variant : result.getResult()) {
            System.out.println("variant = " + variant);
        }
    }

    @Test
    public void testQueryAnonymousWithoutPermissions() throws Exception {
        Query query = new Query(VariantQueryParam.STUDY.key(), studyId);
        thrown.expectMessage("cannot view study");
        thrown.expect(CatalogAuthorizationException.class);
        variantManager.get(query, new QueryOptions(), null);
    }

}
