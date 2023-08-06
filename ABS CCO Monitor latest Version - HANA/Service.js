var Service = require('node-windows').Service;

// Create a new service object
var svc = new Service({
  name:'CCOSupport',
  description: 'CCO Support',
  script: 'C:\\ABS CCO Monitor latest\\bin\\www',
  nodeOptions: [
    '--harmony',
    '--max_old_space_size=8096'
  ]
});

// Listen for the "install" event, which indicates the
// process is available as a service.
svc.on('install',function(){
  svc.start();
});

svc.install();
