const express = require('express');
const app = express();
const portNumber = 8080;

app.use(express.static('public'));

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/public/Frontpage.html');
});

app.listen(portNumber, () => {
    console.log('Server is listening on port ' + portNumber);
});