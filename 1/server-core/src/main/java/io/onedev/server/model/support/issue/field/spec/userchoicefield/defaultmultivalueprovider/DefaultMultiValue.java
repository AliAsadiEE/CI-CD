package io.onedev.server.model.support.issue.field.spec.userchoicefield.defaultmultivalueprovider;

import io.onedev.commons.codeassist.InputSuggestion;
import io.onedev.server.OneDev;
import io.onedev.server.annotation.Editable;
import io.onedev.server.annotation.OmitName;
import io.onedev.server.annotation.Patterns;
import io.onedev.server.buildspecmodel.inputspec.userchoiceinput.choiceprovider.ChoiceProvider;
import io.onedev.server.model.User;
import io.onedev.server.util.EditContext;
import io.onedev.server.web.util.SuggestionUtils;

import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Editable(name="Value")
public class DefaultMultiValue implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> value;
	
	private String applicableProjects;

	@Editable(name="Literal value", order=100)
	@io.onedev.server.annotation.ChoiceProvider("getValueChoices")
	@NotEmpty
	@OmitName
	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}
	
	@Editable(order=200, placeholder="All projects", description="Specify applicable projects separated by space. "
			+ "Use '**', '*' or '?' for <a href='https://docs.onedev.io/appendix/path-wildcard' target='_blank'>path wildcard match</a>. "
			+ "Prefix with '-' to exclude. Leave empty for all projects")
	@Patterns(suggester="suggestProjects", path=true)
	public String getApplicableProjects() {
		return applicableProjects;
	}

	public void setApplicableProjects(String applicableProjects) {
		this.applicableProjects = applicableProjects;
	}

	@SuppressWarnings("unused")
	private static List<InputSuggestion> suggestProjects(String matchWith) {
		return SuggestionUtils.suggestProjectPaths(matchWith);
	}

	@SuppressWarnings("unused")
	private static List<String> getValueChoices() {
		ChoiceProvider choiceProvider = (ChoiceProvider) EditContext.get(1).getInputValue("choiceProvider");
		if (choiceProvider != null && OneDev.getInstance(Validator.class).validate(choiceProvider).isEmpty())
			return choiceProvider.getChoices(true).stream().map(User::getName).collect(Collectors.toList());
		else
			return new ArrayList<>();
	}
	
}
