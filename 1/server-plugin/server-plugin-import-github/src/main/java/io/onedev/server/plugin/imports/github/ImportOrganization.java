package io.onedev.server.plugin.imports.github;

import java.io.Serializable;
import java.util.List;

import io.onedev.server.util.ComponentContext;
import io.onedev.server.web.editable.BeanEditor;
import io.onedev.server.annotation.ChoiceProvider;
import io.onedev.server.annotation.Editable;

@Editable
public class ImportOrganization implements Serializable {

	private static final long serialVersionUID = 1L;
	
	ImportServer server;
	
	private String organization;
	
	private boolean includeForks;

	@Editable(order=100, name="GitHub Organization", description="Select organization to import from. "
			+ "Leave empty to import from repositories under current account")
	@ChoiceProvider("getOrganizationChoices")
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	@Editable(order=200, description="Whether or not to include forked repositories")
	public boolean isIncludeForks() {
		return includeForks;
	}

	public void setIncludeForks(boolean includeForks) {
		this.includeForks = includeForks;
	}

	@SuppressWarnings("unused")
	private static List<String> getOrganizationChoices() {
		BeanEditor editor = ComponentContext.get().getComponent().findParent(BeanEditor.class);
		ImportOrganization setting = (ImportOrganization) editor.getModelObject();
		return setting.server.listOrganizations();
	}
	
}
