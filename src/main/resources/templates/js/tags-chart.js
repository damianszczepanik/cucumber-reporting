$(document).ready(function() {

    var chartData = {
        labels: $chart_categories,
        datasets: [
            {
                label: "Passed",
                backgroundColor: "#92DD96",
                data: $chart_data.get(0)
            },
            {
                label: "Failed",
                backgroundColor: "#F2928C",
                data: $chart_data.get(1)
            },
            {
                label: "Skipped",
                backgroundColor: "#8AF",
                data: $chart_data.get(2)
            },
            {
                label: "Pending",
                backgroundColor: "#F5F28F",
                data: $chart_data.get(3)
            },
            {
                label: "Undefined",
                backgroundColor: "#F5B975",
                data: $chart_data.get(4)
            }
        ]
    };

    var context = document.getElementById("tags-chart");
    window.myBar = new Chart(context, {
        type: "bar",
        data: chartData,
        options: {
            tooltips: {
                mode: "label"
            },
            responsive: true,
            legend: {
                display: false
            },
            scales: {
                xAxes: [{
                    stacked: true
                }],
                yAxes: [{
                    stacked: true
                }]
            }
        }
    });

});