package org.opencb.opencga.catalog.stats.solr.converters;

import org.apache.commons.collections.map.HashedMap;
import org.opencb.opencga.core.models.AnnotationSet;
import org.opencb.opencga.core.models.OntologyTerm;

import java.util.*;

/**
 * Created by wasim on 03/07/18.
 */
public class SolrConverterUtil {


    public static Map<String, Object> populateAnnotations(List<AnnotationSet> annotationSets) {
        Map<String, Object> result = new HashedMap();
        if (annotationSets != null) {
            for (AnnotationSet annotationSet : annotationSets) {
                for (String annotationKey : annotationSet.getAnnotations().keySet()) {
                    Object value = annotationSet.getAnnotations().get(annotationKey);
                    result.put("annotations" + type(value) + annotationSet.getName() + "." + annotationSet.getVariableSetId()
                            + "." + annotationKey, value);
                }
            }
        }
        return result;
    }

    public static List<String> populatePhenotypes(List<OntologyTerm> phenotypes) {
        List<String> phenotypesIds = new ArrayList<>();
        if (phenotypes != null) {
            for (OntologyTerm ontologyTerm : phenotypes) {
                phenotypesIds.add(ontologyTerm.getId());
            }
        }
        return phenotypesIds;
    }

    public static String type(Object object) {

        if (object instanceof Boolean) {
            return "__b__";
        } else if (object instanceof Integer) {
            return "__i__";
        } else if (object instanceof String) {
            return "__s__";
        } else if (object instanceof Double) {
            return "__d__";
        } else if (object instanceof Object) {
            return "__o__";
        } else if (object instanceof Object) {
            return "__a__";
        }
        return "__o__";
    }

    public static Map<String, Set<String>> parseInternalOpenCGAAcls(List<Map<String, Object>> internalPermissions) {
        if (internalPermissions == null) {
            return new HashMap<>();
        }
        Map<String, Set<String>> retPermissions = new HashMap<>(internalPermissions.size());

        internalPermissions.forEach(aclEntry ->
            retPermissions.put((String) aclEntry.get("member"), new HashSet<>((List<String>) aclEntry.get("permissions")))
        );

        return retPermissions;
    }

    public static List<String> getEffectivePermissions(Map<String, Set<String>> studyPermissions,
                                                       Map<String, Set<String>> entityPermissions, String entity) {
        if (studyPermissions == null) {
            studyPermissions = new HashMap<>();
        }
        // entityPermissions are already fine, but we should increase that list with the ones contained in the studyPermissions if they are
        // not overrided by the entry permissions
        Map<String, Set<String>> additionalPermissions = new HashMap<>();
        studyPermissions.forEach((key, value) -> {
            if (!entityPermissions.containsKey(key)) {
                additionalPermissions.put(key, value);
            }
        });

        // FAMILY will need to become VIEW_FAMILIES, SAMPLE will be VIEW_SAMPLES
        String viewEntry = "VIEW_" + (entity.endsWith("Y") ? entity.replace("Y", "IE") : entity) + "S";
        String viewAnnotation = "VIEW_" + entity + "_ANNOTATIONS";

        List<String> permissions = new ArrayList<>((additionalPermissions.size() + entityPermissions.size()) * 3);
        addEffectivePermissions(entityPermissions, permissions, "VIEW", "VIEW_ANNOTATIONS");
        addEffectivePermissions(additionalPermissions, permissions, viewEntry, viewAnnotation);

        return permissions;
    }

    private static void addEffectivePermissions(Map<String, Set<String>> allPermissions, List<String> permissions, String viewPermission,
                                                String viewAnnotationPermission) {
        allPermissions.entrySet().forEach(aclEntry -> {
            List<String> currentPermissions = new ArrayList<>(2);
            if (aclEntry.getValue().contains(viewPermission)) {
                currentPermissions.add(aclEntry.getKey() + "__VIEW");
            }
            if (aclEntry.getValue().contains(viewAnnotationPermission)) {
                currentPermissions.add(aclEntry.getKey() + "__VIEW_ANNOTATIONS");
            }

            if (!currentPermissions.isEmpty()) {
                permissions.addAll(currentPermissions);
            } else {
                permissions.add(aclEntry.getKey() + "__NONE");
            }
        });
    }
}