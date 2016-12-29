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
            ]
        }],
        labels: [
            "Passed",
            "Failed",
            "Skipped",
            "Pending",
            "Undefined"
        ]
    };

    var context = document.getElementById("steps-chart");
    window.myBar = new Chart(context, {
        type: "doughnut",
        data: chartData,
        options: {
            title: {
                display: true,
                fontSize: 20,
                text: "Steps"
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
                        var tooltipPercentage = Math.round(10000 * tooltipData / $report_summary.getSteps()) / 100;
                        return tooltipLabel + ": " + tooltipData + " (" + tooltipPercentage + "%)";
                    }
                }
            }
        }
    });

});