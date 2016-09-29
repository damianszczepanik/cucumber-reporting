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
            ]
        }],
        labels: [
            "Passed",
            "Failed"
        ]
    };

    var context = document.getElementById("scenarios-chart");
    window.myBar = new Chart(context, {
        type: "doughnut",
        data: chartData,
        options: {
            title: {
                display: true,
                fontSize: 20,
                text: "Scenarios"
            },
            circumference: Math.PI,
            rotation: Math.PI,
            cutoutPercentage: 70,
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
                        var tooltipPercentage = Math.round(10000 * tooltipData / $report_summary.getScenarios()) / 100;
                        return tooltipLabel + ": " + tooltipData + " (" + tooltipPercentage + "%)";
                    }
                }
            }
        }
    });

});