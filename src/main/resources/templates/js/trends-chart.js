$(document).ready(function() {

    var buildNumbers = $buildNumbers;

    var failedFeatures = $failedFeatures;
    var totalFeatures = $totalFeatures;

    var failedScenarios = $failedScenarios;
    var totalScenarios = $totalScenarios;

    var passedSteps = $passedSteps;
    var failedSteps = $failedSteps;
    var skippedSteps = $skippedSteps;
    var pendingSteps = $pendingSteps;
    var undefinedSteps = $undefinedSteps;

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
                label: "Passed",
                // TODO: since the first version stored total, not passed values this is displayed with total as not stacked
                // later should use passedFeatures values with stacked mode
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
                label: "Passed",
                // TODO: since the first version stored total, not passed values this is displayed with total as not stacked
                // later should use passedFeatures values with stacked mode
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
                backgroundColor: "#F2928C",
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
            },
            {
                label: "Skipped",
                data: skippedSteps,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#8AF",
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
            },
            {
                label: "Pending",
                data: pendingSteps,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#F5F28F",
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
            },
            {
                label: "Undefined",
                data: undefinedSteps,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#F5B975",
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
            },
            {
                label: "Passed",
                data: passedSteps,
                fill: true,
                lineTension: 0.1,
                backgroundColor: "#92DD96",
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
        data: chartStepsData,
        options: {
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
