/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.tasks.diagnostics.internal.dsl;

import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentSelector;
import org.gradle.api.artifacts.result.DependencyResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.api.specs.Spec;

class DependencyResultSpec implements Spec<DependencyResult> {
    private final String stringNotation;

    public DependencyResultSpec(String stringNotation) {
        this.stringNotation = stringNotation;
    }

    public boolean isSatisfiedBy(DependencyResult candidate) {
        //The matching is very simple at the moment but it should solve majority of cases.
        //It operates using String#contains and it tests either requested or selected module.
        if (candidate instanceof ResolvedDependencyResult) {
            return matchesRequested(candidate) || matchesSelected((ResolvedDependencyResult) candidate);
        } else {
            return matchesRequested(candidate);
        }
    }

    private boolean matchesRequested(DependencyResult candidate) {
        ModuleComponentSelector requested = (ModuleComponentSelector)candidate.getRequested();
        String requestedCandidate = requested.getGroup() + ":" + requested.getModule() + ":" + requested.getVersion();
        return requestedCandidate.contains(stringNotation);
    }

    private boolean matchesSelected(ResolvedDependencyResult candidate) {
        ModuleComponentIdentifier selected = (ModuleComponentIdentifier)candidate.getSelected().getPublishedAs();
        String selectedCandidate = selected.getGroup() + ":" + selected.getModule() + ":" + selected.getVersion();
        return selectedCandidate.contains(stringNotation);
    }
}
