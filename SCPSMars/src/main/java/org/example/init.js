const httpInstance = require('http');
const httpStatusInstance = require('http-status-codes');
const fsInstance = require('fs');
const portNumber = 8080;

// Create a server instance
const httpServer = httpInstance.createServer((req, res) => {

    // Write a response to the client
    res.write(200, {
        "Content-Type": "text/html"
    });
    readFile(redirectToHtml(`Frontpage`), res);
    res.statusCode = 200;

    // End the response
    res.end();
});

// Setup the server to listen on port 8080
httpServer.listen(portNumber, () => {
    console.log('Server is listening on port ' + portNumber);
});

// Read file
const readFile = (file_path, res) => {
    if (fsInstance.existsSync(file_path)) {
        fsInstance.readFile(file_path, (error, data) => {
            if (error) {
                console.log(error);
                handleError(res);
                return;
            }
            res.write(data);
            res.end();
        });
    } else {
        handleError(res);
    }
};