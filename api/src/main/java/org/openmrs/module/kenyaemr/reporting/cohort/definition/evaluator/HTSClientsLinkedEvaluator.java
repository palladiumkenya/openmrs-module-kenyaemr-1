package org.openmrs.module.kenyaemr.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.HTSClientsCohortDefinition;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.HTSLinkedClientsCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

/**
 * Evaluator for Current on ART
 */
@Handler(supports = {HTSLinkedClientsCohortDefinition.class})
public class HTSClientsLinkedEvaluator implements CohortDefinitionEvaluator {

    private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	EvaluationService evaluationService;

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		HTSLinkedClientsCohortDefinition definition = (HTSLinkedClientsCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

		Cohort newCohort = new Cohort();

		String qry=" select patient_id\n" +
				"FROM (\n" +
				"  SELECT\n" +
				"    t.patient_id  patient_id,\n" +
				"    pp.patient_id enrolled,\n" +
				"    l.patient_id  linked,\n" +
				"    p.name program\n" +
				"  FROM kenyaemr_etl.etl_hts_test t\n" +
				"    LEFT OUTER JOIN patient_program pp ON pp.patient_id = t.patient_id\n" +
				"    LEFT JOIN program p ON p.program_id = pp.program_id AND p.name = \"HIV\"\n" +
				"    LEFT OUTER JOIN kenyaemr_etl.etl_hts_referral_and_linkage l ON l.patient_id = t.patient_id\n" +
				") l\n" +
				"where (enrolled is not null and program is not null) or linked is not null;";

		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);

		List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);

		newCohort.setMemberIds(new HashSet<Integer>(ptIds));


        return new EvaluatedCohort(newCohort, definition, context);
    }

}
