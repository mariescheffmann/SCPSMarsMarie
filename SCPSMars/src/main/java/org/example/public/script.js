const url = 'http://localhost:8000';

setInterval( function() {
    fetchDay();
    fetchWeek();
    fetchCloudToGround();
    fetchCloudToCloud();
}, 1000);

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

function fetchCloudToGround() {
    return fetch(url + '/api/cloudToGround')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .then(data => {
            //
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
            //
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};
