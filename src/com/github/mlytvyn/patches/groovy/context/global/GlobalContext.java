package com.github.mlytvyn.patches.groovy.context.global;

import com.github.mlytvyn.patches.groovy.EmailComponentTemplateEnum;
import com.github.mlytvyn.patches.groovy.EmailTemplateEnum;
import com.github.mlytvyn.patches.groovy.EnvironmentEnum;
import com.github.mlytvyn.patches.groovy.SiteEnum;
import com.github.mlytvyn.patches.groovy.SolrEnum;
import com.github.mlytvyn.patches.groovy.context.release.ReleaseContext;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(chain = true, fluent = true)
@Builder(builderMethodName = "internalBuilder")
@RequiredArgsConstructor
public class GlobalContext implements Serializable {

    private static final long serialVersionUID = 1302845848288028643L;
    @NonNull
    @ToString.Include
    private final EnvironmentEnum currentEnvironment;
    private Set<SolrEnum> indexesToBeReindexed = new LinkedHashSet<>();
    private Set<SolrEnum> coresToBeRemoved = new LinkedHashSet<>();
    private Set<EmailTemplateEnum> importEmailTemplates = new LinkedHashSet<>();
    private Map<SolrEnum, Set<String>> partialUpdateProperties = new HashMap<>();
    private Map<EmailComponentTemplateEnum, EnumSet<SiteEnum>> importEmailComponentTemplates = new LinkedHashMap<>();
    // following values executed Before all patches and can be skipped
    @ToString.Include
    private transient List<ReleaseContext> releases = Collections.emptyList();
    private transient boolean removeOrphanedTypes;

    public void schedulePartialUpdate(final SolrEnum solrIndex, final Set<String> indexedProperties) {
        if (indexedProperties.isEmpty()) {
            return;
        }
        partialUpdateProperties().computeIfAbsent(solrIndex, index -> new HashSet<>())
                .addAll(indexedProperties);
    }

    public void fullReIndex(final SolrEnum solrIndex) {
        indexesToBeReindexed().add(solrIndex);
    }

    public void fullReIndex(final List<SolrEnum> solrIndexes) {
        indexesToBeReindexed().addAll(solrIndexes);
    }

    public void removeSolrCores(final List<SolrEnum> solrIndexes) {
        coresToBeRemoved().addAll(solrIndexes);
    }

}
