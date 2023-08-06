let config_data = null;
module.exports = function() {

config_data = {
    CompanyDB :"CHAINS_TEST",
    Password: "1234",
    Username: "B1i",
	Server: "192.168.16.27",
	EmailSubjectSuccess:"No Inconsistent Sales Transactions Check - Marouf CCO",
	EmailSubjectError:"CCO Inconsistent Sales Transactions Check - Marouf CCO",
	Company: "Marouf",
	CCOS:'localhost',
	Port:9797
}

return config_data
}
