package io.onedev.server.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.onedev.server.model.support.EntityWatch;
import io.onedev.server.rest.annotation.Immutable;

@Entity
@Table(
		indexes={@Index(columnList="o_issue_id"), @Index(columnList="o_user_id")},
		uniqueConstraints={@UniqueConstraint(columnNames={"o_issue_id", "o_user_id"})
})
public class IssueWatch extends EntityWatch {

	private static final long serialVersionUID = 1L;

	public static final String PROP_ISSUE = "issue";
	
	public static final String PROP_USER = "user";
	
	public static final String PROP_WATCHING = "watching";
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	@Immutable
	private Issue issue;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	@Immutable
	private User user;
	
	private boolean watching;
	
	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	@Override
	public Issue getEntity() {
		return getIssue();
	}

	@Override
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean isWatching() {
		return watching;
	}

	@Override
	public void setWatching(boolean watching) {
		this.watching = watching;
	}

}
