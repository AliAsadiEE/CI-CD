package io.onedev.server.buildspec.job.trigger;

import io.onedev.server.buildspec.job.Job;
import io.onedev.server.buildspec.job.TriggerMatch;
import io.onedev.server.event.project.ProjectEvent;
import io.onedev.server.event.project.pullrequest.PullRequestChanged;
import io.onedev.server.model.support.pullrequest.changedata.PullRequestMergeData;
import io.onedev.server.annotation.Editable;

@Editable(order=310, name="Pull request merge", description="Job will run on merge commit of target branch and source branch")
public class PullRequestMergeTrigger extends PullRequestTrigger {

	private static final long serialVersionUID = 1L;

	@Override
	protected TriggerMatch triggerMatches(ProjectEvent event, Job job) {
		if (event instanceof PullRequestChanged) {
			PullRequestChanged pullRequestChangeEvent = (PullRequestChanged) event;
			if (pullRequestChangeEvent.getChange().getData() instanceof PullRequestMergeData)
				return triggerMatches(pullRequestChangeEvent.getRequest(), "Pull request is merged");
		}
		return null;
	}

	@Override
	public String getTriggerDescription() {
		return getTriggerDescription("merge");
	}

}
