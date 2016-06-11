$(document).ready(function() {

  var chartData = {
          datasets: [{
              data: [
                     $all_features_passed,
                     $all_features_failed
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

  var ctx = document.getElementById("features-chart");
  window.myBar = new Chart(ctx, {
      type: 'pie',
      data: chartData,
      options: {
          title: {
              display: true,
              fontSize: 20,
              text: 'Features'
          },
          responsive: true,
          legend: {
              display: false
          }
      }
  });

});