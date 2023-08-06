var queries = require('./queries');
const fs = require('fs');
const nodemailer = require('nodemailer');
const schedule = require('node-schedule');
var config = require("./config")();
var https = require('https');
var http = require('http');
console.log(config);
//**********************************************************************//
var sapConnection = {
    "CompanyDB": config.CompanyDB,
    "Password": config.Password,
    "UserName": config.Username
};
var server = config.Server;
console.log(server, sapConnection);
var connectToSAP = function() {
    return new Promise(function(resolve, reject) {
        var options = {
            hostname: server,
            port: 50000,
            path: '/b1s/v1/Login',
            xhrFields: {
                withCredentials: true
            },
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        };

        console.log(options);
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        var req = https.request(options, (res) => {
            let body = '';

            // display returned cookies in header
            var setcookie = res.headers["set-cookie"];
            console.log('setcookie', setcookie);
            var cookieArray = [];
            if (setcookie) {
                setcookie.forEach(
                    function(cookiestr) {
                        console.log("COOKIE:" + cookiestr);
                        cookieArray.push(cookiestr);
                    }
                );
                cookies.push(cookieArray);
            }
            console.log('cookieArray', cookieArray);
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                if (res.statusCode === 200) {
                    connected = true;
                }

                process.stdout.write(body);
                body = JSON.parse(body);
                if (!body.error) {
                    //  database.push(serverData.CompanyDB);
                    resolve(body);
                    console.log('After Successfully Login');
                } else {
                    console.log("body", body);
                    reject(body);
                }

            });
        });
        req.on('error', (e) => {
            console.error(e);
            reject(e);
        });
        console.log(JSON.stringify(sapConnection));
        req.write(JSON.stringify(sapConnection));
        req.end();

    })
}


var connectToCCOS= function(port,server1,userPayload) {
    return new Promise(function(resolve, reject) {
        var options = {
            hostname: server1,
            port: port,
            path: '/ccos/api/auth/admin',
            xhrFields: {
                withCredentials: true
            },
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        };

        console.log(options);
        //process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        var req = http.request(options, (res) => {
            let body = '';

            // display returned cookies in header
            var setcookie = res.headers["set-cookie"];
            console.log('setcookie', setcookie);
            var cookieArray = [];
        /*    if (setcookie) {
                setcookie.forEach(
                    function(cookiestr) {
                        console.log("COOKIE:" + cookiestr);
                        cookieArray.push(cookiestr);
                    }
                );
               
            }
            console.log('cookieArray', cookieArray);
			*/
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                if (res.statusCode === 200) {
                    connected = true;
                }

                process.stdout.write(body);
                body = JSON.parse(body);
				console.log('ccos login', body);
                if (!body.error) {
                    //  database.push(serverData.CompanyDB);
                    resolve(body);
					 cookies.push(body.session);
                    console.log('After Successfully Login');
                } else {
                    console.log("body", body);
                    reject(body);
                }

            });
        });
        req.on('error', (e) => {
            console.error(e);
            reject(e);
        });
        req.write(JSON.stringify(userPayload));
        req.end();

    })
}


var getCCOSLicenseAssignments= function(port,server1) {
    return new Promise(function(resolve, reject) {
        var options = {
            hostname: server1,
            port: port,
            path: '/ccos/api/ui/mgr/configuration/license-management/license/assignments?top=100',
            xhrFields: {
                withCredentials: true
            },
            method: 'GET',
			headers: {
            'Cookie': `JSESSIONID=${cookies[cookies.length - 1].sessionId}` 
  }
        };

        console.log(options);
 
        var req = http.request(options, (res) => {
            let body = '';
	console.log(cookies);
             if (cookies.length - 1) {
                       options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
             } else {
                        //options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
						
             }
			 console.log(options);
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                if (res.statusCode === 200) {
                    connected = true;
                }

                process.stdout.write(body);
                body = JSON.parse(body);
				console.log('ccos login', body);
                if (!body.error) {
                    //  database.push(serverData.CompanyDB);
                    resolve(body);
                    console.log('After Successfully Login');
                } else {
                    console.log("body", body);
                    reject(body);
                }

            });
        });
        req.on('error', (e) => {
            console.error(e);
            reject(e);
        });
       // req.write();
        req.end();

    })
}


var getCCOSLicenseAssignmentsOld= function(port,server1) {
    return new Promise(function(resolve, reject) {
        var options = {
            hostname: server1,
            port: port,
            path: 'http://localhost:9797/ccos/api/ui/license/active/assignments?top=150',
            xhrFields: {
                withCredentials: true
            },
            method: 'GET',
			headers: {
            'Cookie': `JSESSIONID=${cookies[cookies.length - 1].sessionId}` 
  }
        };

        console.log(options);
 
        var req = http.request(options, (res) => {
            let body = '';
	console.log(cookies);
             if (cookies.length - 1) {
                       options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
             } else {
                        //options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
						
             }
			 console.log(options);
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                if (res.statusCode === 200) {
                    connected = true;
                }

                process.stdout.write(body);
                body = JSON.parse(body);
				console.log('ccos login', body);
                if (!body.error) {
                    //  database.push(serverData.CompanyDB);
                    resolve(body);
                    console.log('After Successfully Login');
                } else {
                    console.log("body", body);
                    reject(body);
                }

            });
        });
        req.on('error', (e) => {
            console.error(e);
            reject(e);
        });
       // req.write();
        req.end();

    })
}


var connectToHANA = function(query) {
    var hdb = require('hdb');
    return new Promise(function(resolve, reject) {
        var databaseConnection = {
            host: server,
            port: 30015,
            databaseName: 'NDB',
            user: 'SYSDEV',
            password: 'SYSdev123',
            initializationTimeout: 15000
        }
        var client = hdb.createClient(databaseConnection);
        client.on('error', function(err) {
            console.error('Network connection error', err);
            reject(err);
        });
        //console.log(client.readyState);
        client.connect(function(err) {
            if (err) {
                console.error('Connect error', err);
                reject(err);
            }
            client.exec(query, function(err, rows) {
                client.end();
                if (err) {
                    console.error('Execute error:', err);
                    reject(err);
                }
                resolve(rows);
            });
        });
    });
};

var createSalesOrder = function(salesOrderData) {


    var documentLines = [];
    var lineNotes;
    var cardCode;
    var today = new Date();
    var salesPayload = {};
    var line = {};
    var uoms = [];

    GetItemCodeUOM(salesOrderData.line_items.map(item => '\'' + item.sku + '\'').join())
        .then(Uoms => {
            uoms = Uoms;
            return CheckCustomerExist(salesOrderData.customer_id)
                .then(data => {
                    console.log('Woo sales Order', salesOrderData);
                    if (data.result != false) {
                        return cardCode = data.CardCode;
                    } else {
                        console.log('here');
                        return CreateCustomer(salesOrderData)
                            .then(data => {
                                data = JSON.parse(data);
                                return cardCode = data.CardCode;
                            })
                    }
                })
        })
        .then(cardCode => {
            salesOrderData.line_items.forEach((item, i, items) => {
                lineNotes = "";
                item.meta_data.forEach(meta => {
                    console.log('meta', meta);
                    if (meta.key != "weight") {
                        lineNotes = meta.key + " - " + meta.value
                    }
                })
                let found = uoms.find(element => element.ItemCode == item.sku);
                console.log("found", found);
                if (found && found.uom == "KG") {

                    documentLines.push({
                        ItemCode: item.sku,
                        Quantity: item.quantity / 1000,
                        FreeText: lineNotes,
                        ItemDescription: item.name,
                        PriceAfterVAT: item.price
                    })
                } else {
                    documentLines.push({
                        ItemCode: item.sku,
                        Quantity: item.quantity,
                        FreeText: lineNotes,
                        ItemDescription: item.name,
                        PriceAfterVAT: item.price
                    })
                }
            })

        })
        .then(lines => {
            salesPayload = {
                "DocType": "dDocument_Items",
                "DocDate": formatDate(salesOrderData.date_created),
                "DocDueDate": formatDate(salesOrderData.date_created),
                "NumAtCard": salesOrderData.id,
                "CardCode": cardCode,
                "TotalDiscount": salesOrderData.discount_total,
                "DocTotal": salesOrderData.total,
                "Comments": salesOrderData.customer_note,
                "AddressExtension": {
                    "ShipToCountry": salesOrderData.shipping.country,
                    "ShipToCity": salesOrderData.shipping.city,
                    "ShipToState": salesOrderData.shipping.state,
                    "ShipToZipCode": salesOrderData.shipping.postcode,
                    "ShipToAddress2": salesOrderData.shipping.address_2
                },
                "DocumentAdditionalExpenses": [{
                    "ExpenseCode": 1,
                    "LineTotal": salesOrderData.shipping_total
                }],
                "DocumentLines": documentLines
            }
            return salesPayload;
        })
        .then(salesPayload => {
            return new Promise(function(resolve, reject) {

                    var options = {
                        hostname: server,
                        port: 50000,
                        path: '/b1s/v1/Orders',
                        xhrFields: {
                            withCredentials: true
                        },
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    };


                    if (cookies.length - 1) {
                        options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
                    } else {
                        options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
                    }

                    console.log('abood', options, cookies.length);
                    //bypass SSL certificate
                    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
                    var req = https.request(options, (res) => {
                        let body = '';
                        res.on('data', (data) => {
                            body += data;
                        });

                        res.on('end', () => {

                            var stats = fs.statSync("Logs/Items.txt");
                            var fileSize = stats.size / (1024 * 1024);

                            var testData = JSON.parse(JSON.stringify(body));
                            testData.deliveryDate = today;

                            if (!testData.error) {
                                console.log('line770', testData);
                                console.log('sales order created');
                                resolve(testData.ItemCode);
                            } else {
                                fs.appendFile('Logs/Items.txt', "- Error on UpdateItem: " + "\n" + "Date: " + new Date() + "\n" + "RocketID:  " + itemdata.id + "\n" + ' Error: ' + JSON.stringify(testData.error) + "- ItemPayload: " + JSON.stringify(updateItemPayLoad) + "\n\n", (err) => {});
                                //var emailBody = "- Date: " + new Date() + "<br> <br>" + "- Error on UpdateItem: " + JSON.stringify(testData.error) + "<br> <br>" + "- ItemPayload: " + JSON.stringify(updateItemPayLoad) + "<br> <br>";
                                //sendEmail(emailBody.bold());
                                console.log(testData);
                                reject(testData);
                            }
                        });
                    });

                    req.on('error', (e) => {
                        console.log("EEEEE", e);
                        reject(e);
                    });
                    console.log("SAP Sales Order", salesPayload);
                    req.write(JSON.stringify(salesPayload));
                    req.end();
                })
                .then(result => {
                    console.log('final', result);
                    return result;
                })
        })
}

var createOrUpdateItem = function(itemData) {
    var today = new Date();
    var ItemPrices = [];
    var barCode;
    var groupName;
    var uom;
    var uomEntry;
    var invntItem;
    var tax;
    var url = "delico.store";


    var itemPayLoad = {
        "SKU": itemData.sku,
        "NameAR": itemData.name,
        "Name": itemData.foreignname,
        "Qty": itemData.stock_quantity_onhand - itemData.stock_quantity_commited,
        "Active": itemData.active == 'Y' ? "1" : "0",
        "Description": itemData.description || "",
        "UOM": itemData.uom,
        "Price": String(itemData.regular_price),
        "images": [
            `https://development.b1pro.com:7777/${itemData.sku}.png`
        ]
    };
    console.log("updateItemPayLoad", itemPayLoad);
    return new Promise(function(resolve, reject) {

        var options = {
            hostname: url,
            port: 443,
            path: encodeURI(`/wp-json/SAPintegration/addORupdate`),
            method: 'post',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json'
            }
        };

        var req = https.request(options, (res) => {
            let body = '';
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                console.log('line293', itemData, body);
                var stats = fs.statSync("Logs/Items.txt");
                var fileSize = stats.size / (1024 * 1024);

                var testData = JSON.parse(JSON.stringify(body));
                testData.deliveryDate = today;
                if (!testData.error) {
                    //console.log('line770',testData);
                    resolve(testData.ItemCode);
                } else {
                    fs.appendFile('Logs/Items.txt', "- Error on UpdateItem: " + "\n" + "Date: " + new Date() + "\n" + "RocketID:  " + itemdata.id + "\n" + ' Error: ' + JSON.stringify(testData.error) + "- ItemPayload: " + JSON.stringify(updateItemPayLoad) + "\n\n", (err) => {});
                    //	var emailBody = "- Date: " + new Date() + "<br> <br>" + "- Error on UpdateItem: " + JSON.stringify(testData.error) + "<br> <br>" + "- ItemPayload: " + JSON.stringify(updateItemPayLoad) + "<br> <br>";
                    //	sendEmail(emailBody.bold());
                    reject(testData);
                }
            });
        });

        req.on('error', (e) => {
            console.log("EEEEE", e);
            reject(e);
        });
        console.log('before hitting woo', itemPayLoad)
        req.write(JSON.stringify(itemPayLoad));
        req.end();
    })
}



var connecttoHana = function(query) {
    var hdb = require('hdb');
    return new Promise(function(resolve, reject) {
        var client = hdb.createClient({
            host: server,
            port: 30015,
            databaseName: 'NDB',
            user: 'SYSDEV',
            password: 'SYSdev123',
            initializationTimeout: 15000
        });

        client.on('error', function(err) {
            console.error('Network connection error', err);
            reject(err);
        });

        client.connect(function(err) {
            if (err) {
                console.error('Connect error', err);
                reject(err);
            }
            client.exec(query, function(err, rows) {
                client.end();
                if (err) {
                    console.error('Execute error:', err);
                    reject(err);
                }
                resolve(rows);
            });
        });
    });
};


const {
    resource
} = require('./app');
const {
    basename
} = require('path');

var cookies = [];


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


var connectToSAP = function() {

    return new Promise(function(resolve, reject) {
        var options = {
            hostname: server,
            port: 50000,
            path: '/b1s/v1/Login',
            xhrFields: {
                withCredentials: true
            },
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        };

        console.log(options);
        //bypass SSL certificate
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        var req = https.request(options, (res) => {
            let body = '';

            // display returned cookies in header
            var setcookie = res.headers["set-cookie"];
            console.log('setcookie', setcookie);
            var cookieArray = [];
            if (setcookie) {
                setcookie.forEach(
                    function(cookiestr) {
                        console.log("COOKIE:" + cookiestr);
                        cookieArray.push(cookiestr);
                    }
                );
                cookies.push(cookieArray);
            }
            console.log('cookieArray', cookieArray);
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                if (res.statusCode === 200) {
                    connected = true;
                }

                process.stdout.write(body);
                body = JSON.parse(body);
                if (!body.error) {
                    //  database.push(serverData.CompanyDB);
                    resolve(body);
                    console.log('After Successfully Login');
                } else {
                    console.log("body", body);
                    reject(body);
                }

            });
        });

        req.on('error', (e) => {
            console.error(e);
            reject(e);
        });

        req.write(JSON.stringify(sapConnection));
        req.end();
    });

};


var sendEmail = function(emailBody, subject) {

    const transporter = nodemailer.createTransport({
        host: 'smtp.gmail.com',
        port: 587,
        auth: {
            user: 'support@e2abs.com', //'support@e2abs.com',
            pass: 'ftfrvydoduocxgey' //'&3E25qFq9X',
        },
    });

    transporter.sendMail({
        from: "support@e2abs.com", // sender address
        to: "ccoalert@e2abs.com, n.altorbaq@maroufcoffee.com", // list of receivers , mlubani@e2abs.com, tmansi@beyondgameshq.com, jihad1968@gmail.com
        subject: subject, // Subject line
        //text: "Please check the error as per below: \n" + emailBody, // plain text body
        html: "Dears, <br> <br> This is an automated email to check for inconsistent cco sales transactions: <br> <br>" + emailBody, // html body
    }).then(info => {
        return info;
    }).catch(console.error);

};

var sendCCOSEmail = function(emailBody, subject) {

    const transporter = nodemailer.createTransport({
        host: 'smtp.gmail.com',
        port: 587,
        auth: {
            user: 'support@e2abs.com', //'support@e2abs.com',
            pass: 'ftfrvydoduocxgey' //'&3E25qFq9X',
        },
    });

    transporter.sendMail({
        from: "support@e2abs.com", // sender address
        to: "ccolicense@e2abs.com", // list of receivers , mlubani@e2abs.com, tmansi@beyondgameshq.com, jihad1968@gmail.com
        subject: subject, // Subject line
        //text: "Please check the error as per below: \n" + emailBody, // plain text body
        html: "Dears, <br> <br> This is an automated email to count number of licensed POS <br> <br>" + emailBody, // html body
    }).then(info => {
        return info;
    }).catch(console.error);

};


var CreateCustomer = function(salesOrderData) {

    var today = new Date();
    var addBPPayLoad = {
        "CardName": salesOrderData.billing.first_name + ' ' + salesOrderData.billing.last_name,
        "CardType": "C",
        "GroupCode": "100",
        "Series": "71",
        // "Currency":data.Currency,
        "EmailAddress": salesOrderData.billing.email,
        "Cellular": salesOrderData.billing.phone,
        "Phone1": salesOrderData.billing.phone,
        "AdditionalID": salesOrderData.customer_id,
        "BPAddresses": [{
            "AddressName2": salesOrderData.billing.address_2,
            "AddressName": salesOrderData.billing.address_1,
            "City": salesOrderData.billing.city,
            "Country": salesOrderData.billing.country,
            "ZipCode": salesOrderData.billing.postcode,
            "State": salesOrderData.billing.state
            //"BuildingFloorRoom": salesOrderData.billing.
        }]
    };

    return new Promise(function(resolve, reject) {

        var options = {
            hostname: server,
            port: 50000,
            path: '/b1s/v1/BusinessPartners',
            method: 'post',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
                'Cookie': `${cookies[cookies.length-1]}'`
            }
        };
        if (cookies.length - 1) {
            options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
        }
        //bypass SSL certificate
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        var req = https.request(options, (res) => {
            let body = '';
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                //  process.stdout.write(body);
                var testData = JSON.parse(JSON.stringify(body));
                testData.deliveryDate = today;
                console.log('test 340', testData.deliveryDate, testData.DocDate)
                if (!testData.error) {
                    console.log('line 347', testData);
                    resolve(testData);
                } else {
                    console.log(body);
                    fs.appendFile('Logs/Customer.txt', "- Error on CreateCustomer: " + "\n" + "Date: " + new Date() + "\n" + "RocketID:  " + data.id + "\n" + ' Error: ' + JSON.stringify(testData.error) + "\n\n", (err) => {});
                    reject(testData);
                }
            });
        });

        req.on('error', (e) => {
            console.log("EEEEE", e);
            reject(e);
        });

        req.write(JSON.stringify(addBPPayLoad));
        req.end();
    });
}

var UpdateCustomer = function(data, CardCode) {

    var today = new Date();
    var UpdateBPPayLoad = {
        "CardName": data.first_name + ' ' + data.last_name,
        "CardType": "C",
        "EmailAddress": data.email,
        "GroupCode": "105",
        "Series": "70",
        "U_RocketID": data.id,
        "Cellular": data.phone_numbers[0].number,
        "Phone1": data.phone_numbers[0].number
    };

    return new Promise(function(resolve, reject) {
        console.log("UpdateBPPayLoad", UpdateBPPayLoad);

        var options = {
            hostname: server,
            port: 50000,
            path: encodeURI(`/b1s/v1/BusinessPartners('${CardCode}')`),
            method: 'patch',
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
                'Cookie': `${cookies[cookies.length-1]}'`
            }
        };
        if (cookies.length - 1) {
            options.headers["Cookie"] = cookies[cookies.length - 1].join("; ");
        }
        //bypass SSL certificate
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        var req = https.request(options, (res) => {
            let body = '';
            res.on('data', (data) => {
                body += data;
            });

            res.on('end', () => {
                //  process.stdout.write(body);
                var testData = JSON.parse(JSON.stringify(body));
                testData.deliveryDate = today;
                console.log('test 340', testData.deliveryDate, testData.DocDate)
                if (!testData.error) {
                    console.log('line 347', testData.error);
                    resolve(testData);
                } else {
                    console.log(body);
                    fs.appendFile('Logs/Customer.txt', "- Error on UpdateCustomer: " + "\n" + "Date: " + new Date() + "\n" + "RocketID:  " + data.id + "\n" + ' Error: ' + JSON.stringify(testData.error) + "\n\n", (err) => {});
                    reject(testData);
                }
            });
        });

        req.on('error', (e) => {
            console.log("EEEEE", e);
            reject(e);
        });

        req.write(JSON.stringify(UpdateBPPayLoad));
        req.end();
    });
}

var checkInconsistentReceipts = function(date) {
    return new Promise((resolve, reject) => {
        return connecttoHana(queries.checkInconsistentReceipts(date))
            .then(result => {
                if (result.length) {
                    resolve(result);
                } else {
                    resolve({
                        "result": false
                    });
                }
            })
            .catch(err => {
                reject(err);
            })
    })

}

var checkLatestReceipt = function() {
    return new Promise((resolve, reject) => {
        return connecttoHana(queries.checkLatestReceipt())
            .then(result => {
                if (result.length) {
                    resolve(result);
                } else {
                    resolve({
                        "result": false
                    });
                }
            })
            .catch(err => {
                reject(err);
            })
    })

}


var checkLatestReceiptForAllPos = function() {
    return new Promise((resolve, reject) => {
        return connecttoHana(queries.checkLatestReceiptForAllPos())
            .then(result => {
                if (result.length) {
                    resolve(result);
                } else {
                    resolve({
                        "result": false
                    });
                }
            })
            .catch(err => {
                reject(err);
            })
    })

}

var countInconsistentReceipts = function(date) {
    return new Promise((resolve, reject) => {
        return connecttoHana(queries.countInconsistentReceipts(date))
            .then(result => {
                if (result.length) {
                    resolve(result);
                } else {
                    resolve({
                        "result": false
                    });
                }
            })
            .catch(err => {
                reject(err);
            })
    })

}

//var emailBody =`<table cellpadding="0" cellspacing="0" width="840" align="center" border="1">
//<tr><th>POSKey</th><th>CashDeskID</th><th>ExternalID</th><th>CreationDateTime</th><th>ErrorMessage</th><th>PayDocType</th></tr>`;
var emailBody =`<table cellpadding="0" cellspacing="0" width="840" align="center" border="1">
<tr><th>Number of Receipts</th><th>Error Message</th></tr>`;
var lastReceipt;


const job = schedule.scheduleJob({hour: 13, minute: 21, dayOfWeek: [0, 1, 2, 3, 4]}, function(){
  console.log('The answer to life, the universe, and everything!');
  emailBody =`<table cellpadding="0" cellspacing="0" width="840" align="center" border="1">
<tr><th>Number of Receipts</th><th>Error Message</th></tr>`;
checkLatestReceipt()
.then(res=>{
	console.log(res);
	lastReceipt = new Date(res[0].CreationDateTime * 1000)
	return countInconsistentReceipts()
})

.then(res =>{
	console.log('Number of Inc receipts:', res.length);
	var totalReceipts=0;
	if(!res.length) {
		emailBody = `<p>Last receipt synced from POS - Time : <span style="color:green">${lastReceipt}</span></p>
		<p>There are <b>NO</b> Inconsistent Receipts</p>`
		sendEmail(emailBody,config.EmailSubjectSuccess);
		return;
	}
	res.forEach((receipt,i,receipts)=>{
		emailBody += `<tr><td style="text-align:center;color:red">${receipt.NumberofReceipts}</td><td>${receipt.ErrorMessage}</td></tr>`
		//emailBody = "- Date: " + new Date() + "<br> <br>" + "- Error on UpdateItem: " + JSON.stringify(testData.error) + "<br> <br>" + "- ItemPayload: " + JSON.stringify(updateItemPayLoad) + "<br> <br>";
		totalReceipts += receipt.NumberofReceipts;
	if(receipts.length-1 == i)
	{
		 emailBody += `</table>`;
		 emailBody = ` <p>'Number of Inconsistent receipts: <span style="color:red">${totalReceipts}</span></p>
		 <p>'Last receipt synced from POS - Time : <span style="color:green">${lastReceipt}</span></p>
		 ${emailBody}`;
		
		sendEmail(emailBody,config.EmailSubjectError);
	}
	})

})
.catch(err=>{
	console.log(err);
})
});
/*
.then(res =>{
	console.log('Number of Inc receipts:', res.length);
	res.forEach((receipt,i,receipts)=>{
		emailBody += `<tr><td>${receipt.POSKey}</td><td>${receipt.CashDeskID}</td><td>${receipt.ExternalID}</td><td>${new Date(receipt.CreationDateTime * 1000)}</td><td>${receipt.ErrorMessage}</td><td>${receipt.PayDocType}</td></tr>`
		//emailBody = "- Date: " + new Date() + "<br> <br>" + "- Error on UpdateItem: " + JSON.stringify(testData.error) + "<br> <br>" + "- ItemPayload: " + JSON.stringify(updateItemPayLoad) + "<br> <br>";

	if(receipts.length-1 == i)
	{
		 emailBody += `</table>`;
		 emailBody = ` <p>'Number of Inconsistent receipts: '${res.length}</p>
		 <p>'Last receipt synced from POS - Time : '${lastReceipt}</p>
		 ${emailBody}`;
		console.log(emailBody);
	sendEmail(emailBody.bold(),"CCO Inconsistent Sales Transactions Check");
	}
	})

})
*/

var numDaysBetween = function(d1, d2) {
  var diff = Math.abs(d1.getTime() - d2.getTime());
  return diff / (1000 * 60 * 60 * 24);
};


	var totalPos;
	var licensed;
	var LicensedIDs = [];
	var UnLicensedIDs = [];
//connectToCCOS(5050,'localhost',{ "lowLevel": false, "userName": "Admin", "secret": "abs@1234" })
const job1 = schedule.scheduleJob({hour: 8, minute: 50, dayOfWeek: [0]}, function(){
 connectToCCOS(config.Port,config.CCOS,{ "lowLevel": false, "userName": "Admin", "secret": "abs@1234" })
.then(res=>{
	return getCCOSLicenseAssignments(config.Port,config.CCOS)
	//return getCCOSLicenseAssignmentsOld(config.Port,config.CCOS)
})
.then(res=>{
	 totalPos = res.count;
	 licensed = 0;
	 LicensedIDs = [];
	 UnLicensedIDs = [];
	if(res && res.resultList){
		res.resultList.forEach((pos,i,totalPosArray)=>{
			if(!'IST1 MDB DL01 SHN TB02 IT mdb2'.includes(pos.id)){
			if(pos.licenseAssignment) {
		LicensedIDs.push(pos.id)
        licensed++;
    } else {
		UnLicensedIDs.push(pos.id)
	}
			}
    if(i == totalPosArray.length-1) {
		console.log('essam');
		return
	}
	})
	}
})
.then(res=>{
	return checkLatestReceiptForAllPos();
})
.then(res=>{
	console.log('checkLatestReceiptForAllPos', res);
	if(res.length) {
		res = res.map(function(row) { return {LastReceipt: new Date(row.CreationDateTime * 1000), CashDeskID: row.CashDeskID}})
		lastReceiptAllPos = res;
	}
	console.log('checkLatestReceiptForAllPos', res);
	
		//res.json({Customer:'Testing ENV CCO',NumberOfPoS:totalPos,TotalLicensed:licensed,TotalUnlicensed:totalPos-licensed})	
	let emailBody = `<p>Company: ${config.Company}</p>
		<p>Number of POS installed: ${UnLicensedIDs.length + LicensedIDs.length || totalPos}</p>
		<p>Number of Licensed POS: ${LicensedIDs.length || licensed}</p>
	    <p>Number of Unlicensed PoS (Overused): ${UnLicensedIDs.length || totalPos-licensed}</p>
		<p>Licensed POS IDs: ${LicensedIDs.toString() || 'N/A'}</p>
		<p>Overused POS IDs: '${UnLicensedIDs.toString()}</p><br><br>`
		
		  emailBody +=`<table cellpadding="0" cellspacing="0" width="840" align="center" border="1">
		  <tr><th>POS Name</th><th>Last Receipt</th></tr>`;

		  	res.forEach((receipt,i,receipts)=>{
				if ( numDaysBetween(new Date(), receipt.LastReceipt) > 30 ) {
		emailBody += `<tr><td style="text-align:center;color:red">${receipt.CashDeskID}</td><td>${receipt.LastReceipt}</td></tr>`
				} else {
		emailBody += `<tr><td style="text-align:center;color:green">${receipt.CashDeskID}</td><td>${receipt.LastReceipt}</td></tr>`

				}
		//emailBody = "- Date: " + new Date() + "<br> <br>" + "- Error on UpdateItem: " + JSON.stringify(testData.error) + "<br> <br>" + "- ItemPayload: " + JSON.stringify(updateItemPayLoad) + "<br> <br>";
	if(receipts.length-1 == i)
	{
		 emailBody += `</table>`;
		sendCCOSEmail(emailBody.bold(),"CCOS PoS License Check")
		return;		
	}
	})
})
.catch(err=>{
	console.log(err);
})
})

module.exports = {
    connecttoHana,
    connectToSAP,
    CreateCustomer,
    UpdateCustomer,
    sendEmail,
    createOrUpdateItem,
    createSalesOrder
};
