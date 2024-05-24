const url = 'http://localhost:8000';

const container = document.getElementById('icon-container');

const xValues = [];
const yValues = [];
const ctx = document.getElementById('myChart').getContext('2d');
const myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: xValues,
        datasets: [{
            label: 'Lightning Strikes',
            fill: false,
            lineTension: 0,
            backgroundColor: '#385592',
            borderColor: '#385592',
            data: yValues
        }]
    },
    options: {}
});

setInterval( function() {
    clearLightnings();
    fetchTenMinutes();
    fetchDay();
    fetchWeek();
    fetchFullWeek();
    fetchCloudToGround();
    fetchCloudToCloud();
}, 600000);

function clearLightnings() {
    container.innerHTML = '';
}

function fetchTenMinutes() {
    return fetch(url + '/api/tenMinutes')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .then(data => {
            document.getElementById("textTenMinutes").innerText = data;
            console.log(data);
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};

function fetchDay() {
    return fetch(url + '/api/day')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .then(data => {
            document.getElementById("textToday").innerText = data;
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};

function fetchWeek() {
    return fetch(url + '/api/week')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .then ((data) => {
            document.getElementById("textThisWeek").innerText = data;
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
    }

function fetchFullWeek() {
    return fetch(url + '/api/fullWeek')
        .then(response => response.text())
        .then(data => {
            xValues.length = 0;
            yValues.length = 0;

            data.slice(1, -1).split(", ").forEach(pair => {
                const [date, lightning] = pair.split("=");
                xValues.push(date);
                yValues.push(Number(lightning));
            });

            myChart.data.labels = xValues;
            myChart.data.datasets[0].data = yValues;
            myChart.update();
        })
        .catch(error => {
            console.error('Error fetching full week data:', error);
        });
}

function fetchCloudToGround() {
    return fetch(url + '/api/cloudToGround')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .then(data => {
            insertIcons(data, 'cloudToGroundLightningSmall');
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};

export const fetchCloudToCloud = () => {
    return fetch(url + '/api/cloudToCloud')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .then(data => {
            insertIcons(data, 'cloudToCloudLightningSmall');
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};

function insertIcons(count, style) {
    for (let i = 0; i < count; i++) {
        const icon = document.createElement('ion-icon');
        icon.name = 'flash';
        icon.className = style;
        container.appendChild(icon);
    }
}
