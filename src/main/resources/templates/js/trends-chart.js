$(document).ready(function() {

    var buildNumbers = $buildNumbers;
    var failedFeatures = $failedFeatures;
    var totalFeatures = $totalFeatures;
    var failedScenarios = $failedScenarios;
    var totalScenarios = $totalScenarios;
    var failedSteps = $failedSteps;
    var totalSteps = $totalSteps;

    var chartFeaturesData = {
        labels: buildNumbers,
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

    var featuresContext = document.getElementById("trends-features-chart");
    window.myBar = new Chart(featuresContext, {
        type: "line",
        data: chartFeaturesData
    });


    var chartScenariosData = {
        labels: buildNumbers,
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

    var scenariosContext = document.getElementById("trends-scenarios-chart");
    window.myBar = new Chart(scenariosContext, {
        type: "line",
        data: chartScenariosData
    });


    var chartStepsData = {
        labels: buildNumbers,
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

    var stepsContext = document.getElementById("trends-steps-chart");
    window.myBar = new Chart(stepsContext, {
        type: "line",
        data: chartStepsData
    });
});
