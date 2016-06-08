$(document).ready(function() {

  var chartData = {
          datasets: [{
              data: [
                     $report_summary.getPassedScenarios(),
                     $report_summary.getFailedScenarios()
                 ],
                 backgroundColor: [
                  "#00B000",
                  "#FF3030"
              ],
          }],
          labels: [
              "Passed",
              "Failed"
          ]
      };

  var ctx = document.getElementById("scenarios-chart");
  window.myBar = new Chart(ctx, {
      type: 'doughnut',
      data: chartData,
      options: {
          title: {
              display: true,
              fontSize: 20,
              text: 'Scenarios'
          },
          circumference: Math.PI,
          rotation: Math.PI,
          cutoutPercentage: 70,
          responsive: true,
          legend: {
              display: false
          }
      }
  });

});