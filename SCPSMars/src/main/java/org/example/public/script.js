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