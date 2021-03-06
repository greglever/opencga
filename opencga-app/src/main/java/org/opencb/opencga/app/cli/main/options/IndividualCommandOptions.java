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

package org.opencb.opencga.app.cli.main.options;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import org.opencb.biodata.models.pedigree.IndividualProperty;
import org.opencb.opencga.app.cli.GeneralCliOptions;
import org.opencb.opencga.app.cli.main.options.commons.AclCommandOptions;
import org.opencb.opencga.app.cli.main.options.commons.AnnotationCommandOptions;

import static org.opencb.opencga.app.cli.GeneralCliOptions.*;

/**
 * Created by sgallego on 6/14/16.
 */
@Parameters(commandNames = {"individuals"}, commandDescription = "Individual commands")
public class IndividualCommandOptions {

    public CreateCommandOptions createCommandOptions;
    public InfoCommandOptions infoCommandOptions;
    public SearchCommandOptions searchCommandOptions;
    public UpdateCommandOptions updateCommandOptions;
    public DeleteCommandOptions deleteCommandOptions;
    public GroupByCommandOptions groupByCommandOptions;
    public SampleCommandOptions sampleCommandOptions;
    public StatsCommandOptions statsCommandOptions;

    public IndividualAclCommandOptions.AclsCommandOptions aclsCommandOptions;
    public IndividualAclCommandOptions.AclsUpdateCommandOptions aclsUpdateCommandOptions;

    public AnnotationCommandOptions.AnnotationSetsCreateCommandOptions annotationCreateCommandOptions;
    public AnnotationCommandOptions.AnnotationSetsSearchCommandOptions annotationSearchCommandOptions;
    public AnnotationCommandOptions.AnnotationSetsDeleteCommandOptions annotationDeleteCommandOptions;
    public AnnotationCommandOptions.AnnotationSetsInfoCommandOptions annotationInfoCommandOptions;
    public AnnotationCommandOptions.AnnotationSetsUpdateCommandOptions annotationUpdateCommandOptions;

    public JCommander jCommander;
    public CommonCommandOptions commonCommandOptions;
    public DataModelOptions commonDataModelOptions;
    public NumericOptions commonNumericOptions;

    public IndividualCommandOptions(CommonCommandOptions commonCommandOptions, DataModelOptions dataModelOptions,
                                    NumericOptions numericOptions, JCommander jCommander) {

        this.commonCommandOptions = commonCommandOptions;
        this.commonDataModelOptions = dataModelOptions;
        this.commonNumericOptions = numericOptions;
        this.jCommander = jCommander;

        this.createCommandOptions = new CreateCommandOptions();
        this.infoCommandOptions = new InfoCommandOptions();
        this.searchCommandOptions = new SearchCommandOptions();
        this.updateCommandOptions = new UpdateCommandOptions();
        this.deleteCommandOptions = new DeleteCommandOptions();
        this.groupByCommandOptions = new GroupByCommandOptions();
        this.sampleCommandOptions = new SampleCommandOptions();
        this.statsCommandOptions = new StatsCommandOptions();

        AnnotationCommandOptions annotationCommandOptions = new AnnotationCommandOptions(commonCommandOptions);
        this.annotationCreateCommandOptions = annotationCommandOptions.getCreateCommandOptions();
        this.annotationSearchCommandOptions = annotationCommandOptions.getSearchCommandOptions();
        this.annotationDeleteCommandOptions = annotationCommandOptions.getDeleteCommandOptions();
        this.annotationInfoCommandOptions = annotationCommandOptions.getInfoCommandOptions();
        this.annotationUpdateCommandOptions = annotationCommandOptions.getUpdateCommandOptions();

        IndividualAclCommandOptions aclCommandOptions = new IndividualAclCommandOptions(commonCommandOptions);
        this.aclsCommandOptions = aclCommandOptions.getAclsCommandOptions();
        this.aclsUpdateCommandOptions = aclCommandOptions.getAclsUpdateCommandOptions();
    }

    public class BaseIndividualsCommand extends StudyOption {

        @ParametersDelegate
        public CommonCommandOptions commonOptions = commonCommandOptions;

        @Parameter(names = {"--individual"}, description = "Individual ID or name", required = true, arity = 1)
        public String individual;

    }

    @Parameters(commandNames = {"create"}, commandDescription = "Create individual.")
    public class CreateCommandOptions extends StudyOption {

        @ParametersDelegate
        public CommonCommandOptions commonOptions = commonCommandOptions;

        @Parameter(names = {"-n", "--name"}, description = "Name", required = true, arity = 1)
        public String name;

        @Parameter(names = {"--family"}, description = "Family", required = false, arity = 1)
        public String family;

        @Parameter(names = {"--father-id"}, description = "FatherId", required = false, arity = 1)
        public String fatherId;

        @Parameter(names = {"--mother-id"}, description = "MotherId", required = false, arity = 1)
        public String motherId;

        @Parameter(names = {"--sex"}, description = "Sex. (MALE, FEMALE, UNKNOWN, UNDETERMINED). Default: UNKNOWN", required = false)
        public String sex = "UNKNOWN";

        @Parameter(names = {"--ethnicity"}, description = "Ethnic group", required = false, arity = 1)
        public String ethnicity;

        @Parameter(names = {"--population-name"}, description = "Population name", required = false, arity = 1)
        public String populationName;

        @Parameter(names = {"--population-description"}, description = "Description of the population", required = false, arity = 1)
        public String populationDescription;

        @Parameter(names = {"--population-subpopulation"}, description = "Subpopulation name", required = false, arity = 1)
        public String populationSubpopulation;

        @Parameter(names = {"--karyotypic-sex"}, description = "Karyotypic sex", required = false, arity = 1)
        public String karyotypicSex;

        @Parameter(names = {"--life-status"}, description = "Life status", required = false, arity = 1)
        public String lifeStatus;

        @Parameter(names = {"--affectation-status"}, description = "Affectation status", required = false, arity = 1)
        public String affectationStatus;

        @Parameter(names = {"-dob", "--date-of-birth"}, description = "Date of birth. Format: yyyyMMdd", arity = 1)
        public String dateOfBirth;
    }

    @Parameters(commandNames = {"info"}, commandDescription = "Get individual information")
    public class InfoCommandOptions extends BaseIndividualsCommand {

        @ParametersDelegate
        public DataModelOptions dataModelOptions = commonDataModelOptions;

    }

    @Parameters(commandNames = {"search"}, commandDescription = "Search for individuals")
    public class SearchCommandOptions extends StudyOption {

        @ParametersDelegate
        public GeneralCliOptions.CommonCommandOptions commonOptions = commonCommandOptions;

        @ParametersDelegate
        public DataModelOptions dataModelOptions = commonDataModelOptions;

        @ParametersDelegate
        public NumericOptions numericOptions = commonNumericOptions;

        @Parameter(names = {"--name"}, description = "name", required = false, arity = 1)
        public String name;

        @Parameter(names = {"--father-id"}, description = "fatherId", required = false, arity = 1)
        public String fatherId;

        @Parameter(names = {"--mother-id"}, description = "motherId", required = false, arity = 1)
        public String motherId;

        @Parameter(names = {"--family"}, description = "family", required = false, arity = 1)
        public String family;

        @Parameter(names = {"--sex"}, description = "Sex", required = false, arity = 1)
        public String sex;

        @Parameter(names = {"--ethnicity"}, description = "Ethnic group", required = false, arity = 1)
        public String ethnicity;

        @Deprecated
        @Parameter(names = {"--population"}, description = "[DEPRECATED] population", required = false, arity = 1)
        public String population;

        @Parameter(names = {"--population-name"}, description = "Population name", required = false, arity = 1)
        public String populationName;

        @Parameter(names = {"--population-description"}, description = "Description of the population", required = false, arity = 1)
        public String populationDescription;

        @Parameter(names = {"--population-subpopulation"}, description = "Subpopulation name", required = false, arity = 1)
        public String populationSubpopulation;

        @Parameter(names = {"--karyotypic-sex"}, description = "Karyotypic sex", required = false, arity = 1)
        public String karyotypicSex;

        @Parameter(names = {"--life-status"}, description = "Life status", required = false, arity = 1)
        public String lifeStatus;

        @Parameter(names = {"--affectation-status"}, description = "Affectation status", required = false, arity = 1)
        public String affectationStatus;

        @Parameter(names = {"--annotation"}, description = "Annotation, e.g: key1=value(,key2=value)", required = false, arity = 1)
        public String annotation;

    }

    @Parameters(commandNames = {"update"}, commandDescription = "Update individual information")
    public class UpdateCommandOptions extends BaseIndividualsCommand {

        @Parameter(names = {"--name"}, description = "name", required = false, arity = 1)
        public String name;

        @Parameter(names = {"--father-id"}, description = "FatherId", required = false, arity = 1)
        public String fatherId;

        @Parameter(names = {"--mother-id"}, description = "MotherId", required = false, arity = 1)
        public String motherId;

        @Parameter(names = {"--family"}, description = "Family", required = false, arity = 1)
        public String family;

        @Parameter(names = {"--sex"}, description = "Sex", required = false)
        public String sex;

        @Parameter(names = {"--ethnicity"}, description = "Ethnic group", required = false, arity = 1)
        public String ethnicity;

        @Parameter(names = {"--population-name"}, description = "Population name", required = false, arity = 1)
        public String populationName;

        @Parameter(names = {"--population-description"}, description = "Description of the population", required = false, arity = 1)
        public String populationDescription;

        @Parameter(names = {"--population-subpopulation"}, description = "Subpopulation name", required = false, arity = 1)
        public String populationSubpopulation;

        @Parameter(names = {"--karyotypic-sex"}, description = "Karyotypic sex", required = false, arity = 1)
        public String karyotypicSex;

        @Parameter(names = {"--life-status"}, description = "Life status", required = false, arity = 1)
        public String lifeStatus;

        @Parameter(names = {"--affectation-status"}, description = "Affectation status", required = false, arity = 1)
        public String affectationStatus;

        @Parameter(names = {"-dob", "--date-of-birth"}, description = "Date of birth. Format: yyyyMMdd", arity = 1)
        public String dateOfBirth;
    }

    @Parameters(commandNames = {"delete"}, commandDescription = "Delete individual information")
    public class DeleteCommandOptions extends BaseIndividualsCommand {

    }

    @Parameters(commandNames = {"group-by"}, commandDescription = "Group individuals by several fields")
    public class GroupByCommandOptions extends StudyOption {

        @ParametersDelegate
        CommonCommandOptions commonOptions = commonCommandOptions;

        @Deprecated
        @Parameter(names = {"--ids"}, description = "[DEPRECATED] Comma separated list of ids.", required = false, arity = 1)
        public String id;

        @Parameter(names = {"--fields"}, description = "Comma separated list of fields by which to group by.", required = true, arity = 1)
        public String fields;

        @Parameter(names = {"--name"}, description = "Comma separated list of names.", required = false, arity = 0)
        public String name;

        @Parameter(names = {"--father-id"}, description = "FatherId", required = false, arity = 1)
        public String fatherId;

        @Parameter(names = {"--mother-id"}, description = "MotherId", required = false, arity = 1)
        public String motherId;

        @Parameter(names = {"--family"}, description = "Family", required = false, arity = 1)
        public String family;

        @Parameter(names = {"--sex"}, description = "Sex", required = false)
        public IndividualProperty.Sex sex;

        @Parameter(names = {"--ethnicity"}, description = "Ethnic group", required = false, arity = 1)
        public String ethnicity;

        @Deprecated
        @Parameter(names = {"--population"}, description = "[DEPRECATED] population", required = false, arity = 1)
        public String population;

        @Parameter(names = {"--population-name"}, description = "Population name", required = false, arity = 1)
        public String populationName;

        @Parameter(names = {"--population-description"}, description = "Description of the population", required = false, arity = 1)
        public String populationDescription;

        @Parameter(names = {"--population-subpopulation"}, description = "Subpopulation name", required = false, arity = 1)
        public String populationSubpopulation;

        @Parameter(names = {"--karyotypic-sex"}, description = "Karyotypic sex", required = false, arity = 1)
        public String karyotypicSex;

        @Parameter(names = {"--life-status"}, description = "Life status", required = false, arity = 1)
        public String lifeStatus;

        @Parameter(names = {"--affectation-status"}, description = "Affectation status", required = false, arity = 1)
        public String affectationStatus;

        @Parameter(names = {"--variable-set"}, description = "Variable set id or name", required = false, arity = 1)
        public String variableSetId;

        @Parameter(names = {"--annotation-set-name"}, description = "Annotation set name.", required = false, arity = 0)
        public String annotationSetName;

        @Parameter(names = {"--annotation"}, description = "Annotation, e.g: key1=value(,key2=value)", required = false, arity = 1)
        public String annotation;
    }

    @Parameters(commandNames = {"samples"}, commandDescription = "Get the samples of a list of individuals.")
    public class SampleCommandOptions extends StudyOption {

        @ParametersDelegate
        public CommonCommandOptions commonOptions = commonCommandOptions;

        @ParametersDelegate
        public DataModelOptions dataModelOptions = commonDataModelOptions;

        @Parameter(names = {"--individual"}, description = "List of individual ids or names", required = true, arity = 1)
        public String individual;
    }

    public class IndividualAclCommandOptions extends AclCommandOptions {

        private AclsUpdateCommandOptions aclsUpdateCommandOptions;

        public IndividualAclCommandOptions(CommonCommandOptions commonCommandOptions) {
            super(commonCommandOptions);
        }

        @Parameters(commandNames = {"acl-update"}, commandDescription = "Update the permissions set for a member")
        public class AclsUpdateCommandOptions extends AclCommandOptions.AclsUpdateCommandOptions {

//            @Parameter(names = {"--individual"}, description = "Comma separated list of individual ids or names", arity = 1)
//            public String individual;

            @Parameter(names = {"--sample"}, description = "Comma separated list of sample ids or file containing the list of ids "
                    + "(one per line)", arity = 1)
            public String sample;

            @Parameter(names = {"--propagate"}, description = "Flag parameter indicating whether to propagate the permissions to the " +
                    "samples related to the individual(s).", arity = 0)
            public boolean propagate;
        }

        public AclsUpdateCommandOptions getAclsUpdateCommandOptions() {
            if (this.aclsUpdateCommandOptions == null) {
                this.aclsUpdateCommandOptions = new AclsUpdateCommandOptions();
            }
            return aclsUpdateCommandOptions;
        }
    }

    @Parameters(commandNames = {"stats"}, commandDescription = "Individual stats")
    public class StatsCommandOptions extends StudyOption {

        @ParametersDelegate
        public CommonCommandOptions commonOptions = commonCommandOptions;

        @Parameter(names = {"--default"}, description = "Flag to calculate default stats", arity = 0)
        public boolean defaultStats;

        @Parameter(names = {"--creation-year"}, description = "Creation year.", arity = 1)
        public String creationYear;

        @Parameter(names = {"--creation-month"}, description = "Creation month (JANUARY, FEBRUARY...).", arity = 1)
        public String creationMonth;

        @Parameter(names = {"--creation-day"}, description = "Creation day.", arity = 1)
        public String creationDay;

        @Parameter(names = {"--creation-day-of-week"}, description = "Creation day of week (MONDAY, TUESDAY...).", arity = 1)
        public String creationDayOfWeek;

        @Parameter(names = {"--status"}, description = "Status.", arity = 1)
        public String status;

        @Parameter(names = {"--life-status"}, description = "Life status.", arity = 1)
        public String lifeStatus;

        @Parameter(names = {"--affectation-status"}, description = "Affectation status.", arity = 1)
        public String affectationStatus;

        @Parameter(names = {"--num-samples"}, description = "Number of samples", arity = 1)
        public String numSamples;

        @Parameter(names = {"--release"}, description = "Release.", arity = 1)
        public String release;

        @Parameter(names = {"--version"}, description = "Version.", arity = 1)
        public String version;

        @Parameter(names = {"--has-father"}, description = "Has father.", arity = 1)
        public Boolean hasFather;

        @Parameter(names = {"--has-mother"}, description = "Has mother.", arity = 1)
        public Boolean hasMother;

        @Parameter(names = {"--num-multiples"}, description = "Number of multiples.", arity = 1)
        public String numMultiples;

        @Parameter(names = {"--multiples-type"}, description = "Type of multiples.", arity = 1)
        public String multiplesType;

        @Parameter(names = {"--sex"}, description = "Sex.", arity = 1)
        public String sex;

        @Parameter(names = {"--karyotypic-sex"}, description = "Karyotypic sex.", arity = 1)
        public String karyotypicSex;

        @Parameter(names = {"--ethnicity"}, description = "Ethnicity.", arity = 1)
        public String ethnicity;

        @Parameter(names = {"--population"}, description = "Population.", arity = 1)
        public String population;

        @Parameter(names = {"--phenotypes"}, description = "Phenotypes.", arity = 1)
        public String phenotypes;

        @Parameter(names = {"--parental-consanguinity"}, description = "Parental consanguinity.", arity = 1)
        public Boolean parentalConsanguinity;

        @Parameter(names = {"--annotation"}, description = "Annotation. See documentation to see the options.", arity = 1)
        public String annotation;

        @Parameter(names = {"--field"}, description = "List of fields separated by semicolons, e.g.: studies;type. For nested "
                + "fields use >>, e.g.: studies>>biotype;type.", arity = 1)
        public String field;

        @Parameter(names = {"--field-range"}, description = "List of field ranges separated by semicolons with the format"
                + " {field_name}:{start}:{end}:{step}, e.g.: sift:0:1:0.2;caddRaw:0:30:1.", arity = 1)
        public String fieldRange;
    }

}
