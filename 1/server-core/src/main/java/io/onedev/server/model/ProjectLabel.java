package io.onedev.server.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.onedev.server.model.support.EntityLabel;
import io.onedev.server.rest.annotation.Api;
import io.onedev.server.rest.annotation.Immutable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(
		indexes={@Index(columnList="o_project_id"), @Index(columnList="o_spec_id")},
		uniqueConstraints={@UniqueConstraint(columnNames={"o_project_id", "o_spec_id"})}
)
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class ProjectLabel extends EntityLabel {

	private static final long serialVersionUID = 1L;

	public static String PROP_PROJECT = "project";
	
	public static String PROP_SPEC = "spec";
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable=false)
	@Api(description = "id of <a href='/~help/api/io.onedev.server.rest.ProjectResource'>project</a>")
	@Immutable
	private Project project;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	@Api(description = "id of <a href='/~help/api/io.onedev.server.rest.LabelSpecResource'>label spec</a>")
	private LabelSpec spec;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public LabelSpec getSpec() {
		return spec;
	}

	public void setSpec(LabelSpec spec) {
		this.spec = spec;
	}

	@Override
	public AbstractEntity getEntity() {
		return getProject();
	}
	
}
