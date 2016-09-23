$(document).ready(function() {

    var buildLabels = ["Build no 5", "Build no 6", "Build no 7", "Build no 8", "Build no 10"];
    var failedFeatures = [0, 59, 1, 0, 4];
    var totalFeatures = [65, 59, 56, 0, 40];
    var failedScenarios = [0, 59, 1, 0, 4];
    var totalScenarios = [6, 90, 6, 0, 40];
    var failedSteps = [0, 590, 10, 20, 40];
    var totalSteps = [650, 590, 560, 0, 20];

    var chartFeaturesData = {
        labels: buildLabels,
        datasets: [
            {
                label: "Failed",
                data: failedFeatures,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#F2928C", // same as failed class in css file
                borderColor: "rgba(192,0,0,1)",
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: "rgba(0,0,0,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 5,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                spanGaps: false,
            },
            {
                label: "Total",
                data: totalFeatures,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#92DD96", // same as passed class in css file
                borderColor: "rgba(0,192,192,1)",
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: "rgba(0,0,0,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 5,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                spanGaps: false
            }
        ]
    };

    var context = document.getElementById("trends-features-chart");
    window.myBar = new Chart(context, {
        type: "line",
        data: chartFeaturesData
    });


    var chartScenariosData = {
        labels: buildLabels,
        datasets: [
            {
                label: "Failed",
                data: failedScenarios,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#F2928C", // same as failed class in css file
                borderColor: "rgba(192,0,0,1)",
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: "rgba(0,0,0,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 5,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                spanGaps: false,
            },
            {
                label: "Total",
                data: totalScenarios,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#92DD96", // same as passed class in css file
                borderColor: "rgba(0,192,192,1)",
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: "rgba(0,0,0,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 5,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                spanGaps: false
            }
        ]
    };

    var context = document.getElementById("trends-scenarios-chart");
    window.myBar = new Chart(context, {
        type: "line",
        data: chartScenariosData
    });


    var chartStepsData = {
        labels: buildLabels,
        datasets: [
            {
                label: "Failed",
                data: failedSteps,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#F2928C", // same as failed class in css file
                borderColor: "rgba(192,0,0,1)",
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: "rgba(0,0,0,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 5,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                spanGaps: false,
            },
            {
                label: "Total",
                data: totalSteps,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#92DD96", // same as passed class in css file
                borderColor: "rgba(0,192,192,1)",
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: "rgba(0,0,0,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 5,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                spanGaps: false
            }
        ]
    };

    var context = document.getElementById("trends-steps-chart");
    window.myBar = new Chart(context, {
        type: "line",
        data: chartStepsData
    });
});
