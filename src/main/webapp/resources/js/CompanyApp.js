/**
 * Created by srividyak on 08/07/15.
 */
TWApp = CommonUtils.isUndefinedOrNull(TWApp) ? {} : TWApp;

TWApp.Company = {
    
    first: true,
    
    getCompanies: function() {
        var success = function(response) {
            var companies = response.companies;
            for (var i = 0; i < companies.length; i++) {
                var company = companies[i];
                company["json"] = JSON.stringify(company);
            }
            var html = $.Mustache.render('companies-template', {"companies": response.companies});
            $('#main-content').html(html);
        };
        var error = function(response) {
            console.log(response);
        };
        CommonUtils.makeGetCall(CommonUtils.urls.getCompanies, success, error);
    },
    
    showAddCompany: function() {
        var html = $.Mustache.render('add-company-template', {newCompany: true});
        $('#company-info-container').html(html);
    },
    
    saveCompany: function() {
        var app = TWApp.Company;
        var companyData = {
            "name": $('#company-name').val(),
            "industry": $('#company-industry').val(),
            "location": $('#company-location').val(),
            "numEmployees": $('#company-numEmployees').val(),
            "website": $('#company-website').val()
        };
        var success = function(response) {
            app.getCompanies();
        };
        var error = function(response) {
            console.log("error");
            console.log(response);
        };
        CommonUtils.makePostCall(CommonUtils.urls.addCompanies, {companies: JSON.stringify([companyData])}, success, error);
    },
    
    bindElements: function() {
        var app = TWApp.Company;
        $('body').delegate('#companies-list', 'change', function(e) {
            var option = $('#companies-list option:selected');
            var companyData = JSON.parse(option.attr('data-company-json'));
            var html = $.Mustache.render('add-company-template', companyData);
            $('#company-info-container').html(html);
        });
        $('body').delegate('#save-company', 'click', function(e) {
            app.saveCompany();
        });
        $('body').delegate('#add-company-btn', 'click', function(e) {
            app.showAddCompany();
        });
    },

    init: function() {
        if (this.first === true) {
            $.Mustache.addFromDom('companies-template');
            $.Mustache.addFromDom('add-company-template');
            this.getCompanies();
            this.bindElements();
            this.first = false;
        } else {
            this.getCompanies();
        }
    }
};