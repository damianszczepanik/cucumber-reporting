$(document).ready(function() {

  var chartData = {
          datasets: [{
              data: [
                  $report_summary.getPassedSteps(),
                  $report_summary.getFailedSteps(),
                  $report_summary.getSkippedSteps(),
                  $report_summary.getPendingSteps(),
                  $report_summary.getUndefinedSteps()
              ],
              backgroundColor: [
                  "#00B000",
                  "#FF3030",
                  "#88AAFF",
                  "#F5F28F",
                  "#F5B975"
              ],
          }],
          labels: [
              "Passed",
              "Failed",
              "Skipped",
              "Pending",
              "Undefined"
          ]
      };

  var ctx = document.getElementById("steps-chart");
  window.myBar = new Chart(ctx, {
      type: 'doughnut',
      data: chartData,
      options: {
          title: {
              display: true,
              fontSize: 20,
              text: 'Steps'
          },
          responsive: true,
          legend: {
              display: false
          }
      }
  });

});