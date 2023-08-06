var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser')
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var queries = require('./queries');
var connections = require('./Connection');
const Connection = require('./Connection');
const fs = require('fs');
var request = require('request');
const { response } = require('express');
const e = require('express');
const { json } = require('body-parser');
const nodemailer = require('nodemailer');
const { rejects } = require('assert');
const { Console } = require('console');
const date = require('date-and-time');

const child_process = require("child_process");



var cookie = [];

var app = express();

var access_token;

var running = 0;

app.use(bodyParser.urlencoded({ extended: false }))


app.use(bodyParser.json())

var dir = './Logs'
if (!fs.existsSync(dir)){
  fs.mkdirSync(dir);
}


var files = ['Logs/Customer.txt', 'Logs/Items.txt' , 'Logs/Invoice.txt' , 'Logs/StartandEndTime.txt'];

files.forEach(element => {
  if (!fs.existsSync(element)){
    fs.writeFile(element, '\r\n', (err) => {});
  }
});

function formatDate(date) {
  var d = new Date(date),
      month = '' + (d.getMonth() + 1),
      day = '' + d.getDate(),
      year = d.getFullYear();

  if (month.length < 2)
      month = '0' + month;
  if (day.length < 2)
      day = '0' + day;

  return [year, month, day].join('');
}

app.get('/', function(req, res, next) {
  console.log('you added or updated order', req.body, req.query, req.params);
res.render('test', { html: 'Chains Web Service is Running' })
});


app.post('/items', function(req, res, next) {
  console.log('you added or updated item', req.body, req.query);
if(req.query && req.query.inv){
  req.body.forEach((item, i) => {
    connections.createOrUpdateItem(item)
    .then(result=>{
      res.send(result);
    })
    .catch(err=>{
      console.log(err);
    })
  });

} else {
connections.createOrUpdateItem(req.body)
.then(result=>{
  res.send(result);
})
.catch(err=>{
  console.log(err);
})
}
});


app.post('/order', function(req, res, next) {
  console.log('you added or updated order', req.body, req.query, req.params);

  connections.connectToSAP()
  .then(result => {
    connections.createSalesOrder(req.body)
  })
  .then(()=>{
    res.send(req.body);
  })
  .catch(err=>{
    console.log(err);
  })
});


app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});



module.exports = app;
