const express = require("express");
const app = express();
const port = 8083;

app.use(express.static(__dirname+"/public"));

app.get("/", (req, res) => {
    res.sendFile(__dirname+"/views/index.html");
});

app.listen(port, () => {
    console.log(port);
});