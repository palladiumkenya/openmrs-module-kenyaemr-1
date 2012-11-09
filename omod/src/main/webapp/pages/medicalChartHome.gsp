<%
	ui.decorateWith("kenyaemr", "standardKenyaEmrPage")
%>

<style type="text/css">
.col2 {
	width: 48%;
	float: left;
}
#end-of-day {
	display: none;
}
#end-of-day .spaced {
	padding: 0.3em;
}
</style>

<div class="col2">
	<h1>Medical Chart App</h1>
	<h3>Welcome!</h3>
	
	${ ui.includeFragment("uilibrary", "widget/button", [ icon: "buttons/patient_search.png", iconProvider: "kenyaemr", label: "Find a Patient", href: ui.pageLink("kenyaemr", "medicalChartSearch") ]) }
</div>

<div class="col2">
	<h2>Recently-viewed patients</h2>
	${ ui.includeFragment("kenyaemr", "patientList", [ id: "recentlyViewedPatients", page: "medicalChartViewPatient" ]) }
	
	<br/>
	<div id="end-of-day">
		<h3>End-of-Day Tasks</h3>
		Close all open visits of the following types:
		<form method="post" action="${ ui.actionLink("kenyaemr", "registrationUtil", "closeActiveVisits") }">
			<div class="form-data">
			</div>
			<input type="submit" value="Close Visits"/>
		</form>
	</div>
</div>


<script type="text/javascript">
	getJsonAsEvent(ui.fragmentActionLink('kenyaemr', 'medicalChartUtil', 'recentlyViewed'), 'recentlyViewedPatients/show');
</script>