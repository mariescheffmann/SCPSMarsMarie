const url = 'http://localhost:8000';

export const fetchDay = () => {
    return fetch(url + '/api/day')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};

export const fetchWeek = () => {
    return fetch(url + '/api/week')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};

export const fetchCloudToGround = () => {
    return fetch(url + '/api/cloudToGround')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};

export const fetchCloudToClous = () => {
    return fetch(url + '/api/cloudToCloud')
        .then(response => {
            console.log('Response:', response);
            return response.text();
        })
        .catch(error => {
            console.error('Error fetching state:', error);
            throw error;
        });
};
