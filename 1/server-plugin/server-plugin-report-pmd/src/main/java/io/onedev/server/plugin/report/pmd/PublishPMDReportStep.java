package io.onedev.server.plugin.report.pmd;

import io.onedev.commons.codeassist.InputSuggestion;
import io.onedev.commons.utils.FileUtils;
import io.onedev.commons.utils.PlanarRange;
import io.onedev.commons.utils.TaskLogger;
import io.onedev.server.annotation.Editable;
import io.onedev.server.annotation.Interpolative;
import io.onedev.server.annotation.Patterns;
import io.onedev.server.buildspec.BuildSpec;
import io.onedev.server.buildspec.step.StepGroup;
import io.onedev.server.codequality.BlobTarget;
import io.onedev.server.codequality.CodeProblem;
import io.onedev.server.codequality.CodeProblem.Severity;
import io.onedev.server.model.Build;
import io.onedev.server.plugin.report.problem.PublishProblemReportStep;
import io.onedev.server.util.XmlUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.unbescape.html.HtmlEscape.escapeHtml5;

@Editable(order=10000, group=StepGroup.PUBLISH, name="PMD Report")
public class PublishPMDReportStep extends PublishProblemReportStep {

	private static final long serialVersionUID = 1L;
	
	private static final int TAB_WIDTH = 8;
	
	@Editable(order=100, description="Specify PMD result xml file relative to <a href='https://docs.onedev.io/concepts#job-workspace'>job workspace</a>, "
			+ "for instance, <tt>target/pmd.xml</tt>. Use * or ? for pattern match")
	@Interpolative(variableSuggester="suggestVariables")
	@Patterns(path=true)
	@NotEmpty
	@Override
	public String getFilePatterns() {
		return super.getFilePatterns();
	}

	@Override
	public void setFilePatterns(String filePatterns) {
		super.setFilePatterns(filePatterns);
	}
	
	@SuppressWarnings("unused")
	private static List<InputSuggestion> suggestVariables(String matchWith) {
		return BuildSpec.suggestVariables(matchWith, true, true, false);
	}
	
	@Override
	protected List<CodeProblem> process(Build build, File inputDir, File reportDir, TaskLogger logger) {
		int baseLen = inputDir.getAbsolutePath().length() + 1;
		SAXReader reader = new SAXReader();
		XmlUtils.disallowDocTypeDecl(reader);

		List<CodeProblem> problems = new ArrayList<>();
		for (File file: getPatternSet().listFiles(inputDir)) {
			String relativePath = file.getAbsolutePath().substring(baseLen);
			logger.log("Processing PMD report '" + relativePath + "'...");
			try {
				String xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
				Document doc = reader.read(new StringReader(XmlUtils.stripDoctype(xml)));

				for (Element fileElement: doc.getRootElement().elements("file")) {
					var filePath = fileElement.attributeValue("name");
					String blobPath = build.getBlobPath(filePath);
					if (blobPath != null) {
						for (Element violationElement: fileElement.elements("violation")) {
							int beginLine = Integer.parseInt(violationElement.attributeValue("beginline"));
							int endLine = Integer.parseInt(violationElement.attributeValue("endline"));
							int beginColumn = Integer.parseInt(violationElement.attributeValue("begincolumn"));
							int endColumn = Integer.parseInt(violationElement.attributeValue("endcolumn"));
							PlanarRange location = new PlanarRange(beginLine-1, beginColumn-1, endLine-1, endColumn, TAB_WIDTH);
							
							String type = violationElement.attributeValue("rule");
							
							Severity severity;
							int priority = Integer.parseInt(violationElement.attributeValue("priority"));
							if (priority <= 2)
								severity = Severity.HIGH;
							else if (priority <= 3)
								severity = Severity.MEDIUM;
							else
								severity = Severity.LOW;
							
							String message = escapeHtml5(type + ": " + violationElement.getText());
							problems.add(new CodeProblem(severity, new BlobTarget(blobPath, location), message));
						}
					} else {
						logger.warning("Unable to find blob path for file: " + filePath);						
					}
				}
			} catch (DocumentException e) {
				logger.warning("Ignored SpotBugs report '" + relativePath + "' as it is not a valid XML");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return problems;
	}

}
