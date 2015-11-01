/**
 * Created by srividyak on 08/07/15.
 */
TWApp = typeof TWApp === "undefined" || TWApp == null ? {} : TWApp;
TWApp.apiUrlPrefix = "/TapWisdom-1.0/admin/api/v1"

CommonUtils = function() {
    var app = {
        isUndefinedOrNull: function(o) {
            return typeof o === "undefined" || o == null;
        },
        urls: {
            "isLoggedIn": TWApp.apiUrlPrefix + "/adminuser/isLoggedIn",
            "login": TWApp.apiUrlPrefix + "/adminuser/login",
            "getCompanies": TWApp.apiUrlPrefix + "/companies",
            "addCompanies": TWApp.apiUrlPrefix + "/companies",
            "getQuestionsForCompany": TWApp.apiUrlPrefix + "/questions/company",
            "saveQuestions": TWApp.apiUrlPrefix + "/questions",
            "editQuestion": TWApp.apiUrlPrefix + "/questions/edit"
        },
        makeGetCall: function(url, successCb, errorCb) {
            $.ajax({
                url: url,
                dataType: "json",
                method: "GET",
                success: function(response) {
                    if (response.statusCode == 0) {
                        successCb(response);
                    } else {
                        errorCb(response);
                    }
                },
                error: function(response) {
                    errorCb(response);
                }
            });
        },
        makePostCall: function(url, data, successCb, errorCb) {
            $.ajax({
                url: url,
                data: JSON.stringify(data),
                dataType: "json",
                contentType: "application/json",
                method: "POST",
                success: function(response) {
                    if (response.statusCode == 0) {
                        successCb(response);
                    } else {
                        errorCb(response);
                    }
                },
                error: function(response) {
                    errorCb(response);
                }
            });
        }
    };
    return {
        isUndefinedOrNull: app.isUndefinedOrNull,
        makeGetCall: app.makeGetCall,
        makePostCall: app.makePostCall,
        urls: app.urls
    }
}();