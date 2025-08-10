package io.onedev.server.model.support.issue.field.spec.userchoicefield.defaultvalueprovider;

import io.onedev.server.annotation.Editable;
import io.onedev.server.annotation.OmitName;
import io.onedev.server.model.Project;
import io.onedev.commons.utils.match.Matcher;
import io.onedev.commons.utils.match.PathMatcher;
import io.onedev.server.util.patternset.PatternSet;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Editable(order=100, name="Use specified default value", 
		description = "For a particular project, the first matching entry will be used")
public class SpecifiedDefaultValue implements DefaultValueProvider {

	private static final long serialVersionUID = 1L;

	private List<DefaultValue> defaultValues = new ArrayList<>();

	@Editable
	@Size(min=1, message="At least one entry should be specified")
	@OmitName
	public List<DefaultValue> getDefaultValues() {
		return defaultValues;
	}

	public void setDefaultValues(List<DefaultValue> defaultValues) {
		this.defaultValues = defaultValues;
	}

	@Override
	public String getDefaultValue() {
		Project project = Project.get();
		if (project != null) {
			Matcher matcher = new PathMatcher();
			for (DefaultValue defaultValue : getDefaultValues()) {
				if (defaultValue.getApplicableProjects() == null
						|| PatternSet.parse(defaultValue.getApplicableProjects()).matches(matcher, project.getPath())) {
					return defaultValue.getValue();
				}
			}
		}
		return null;
	}

}
