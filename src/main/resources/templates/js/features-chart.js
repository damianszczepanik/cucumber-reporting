$(document).ready(function() {

    var chartData = {
        datasets: [{
            data: [
                   $report_summary.getPassedFeatures(),
                   $report_summary.getFailedFeatures()
               ],
               backgroundColor: [
                "#00B000",
                "#FF3030"
            ]
        }],
        labels: [
            "Passed",
            "Failed"
        ]
    };

    var context = document.getElementById("features-chart");
    window.myBar = new Chart(context, {
        type: "pie",
        data: chartData,
        options: {
            title: {
                display: true,
                fontSize: 20,
                text: "Features"
            },
            responsive: true,
            legend: {
                display: false
            },
            tooltips: {
                callbacks: {
                    label: function(tooltipItem, data) {
                        var allData = data.datasets[tooltipItem.datasetIndex].data;
                        var tooltipLabel = data.labels[tooltipItem.index];
                        var tooltipData = allData[tooltipItem.index];
                        var tooltipPercentage = Math.round(10000 * tooltipData / $all_features.size()) / 100;
                        return tooltipLabel + ": " + tooltipData + " (" + tooltipPercentage + "%)";
                    }
                }
            }
        }
    });

});