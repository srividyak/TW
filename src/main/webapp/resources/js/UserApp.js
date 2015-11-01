/**
 * Created by srividyak on 08/07/15.
 */
TWApp = CommonUtils.isUndefinedOrNull(TWApp) ? {} : TWApp;

TWApp.User = function() {
    var showMenuBar = function() {
        var html = $.Mustache.render('user-menu-bar-template', {});
        $('#user-menu-bar').html(html);
        TWApp.Company.init();
    };
    
    var callbacks = {
        isLoggedInSuccess: function(response) {
            showMenuBar();
        },
        isLoggedInError: function(response) {
            if (response.statusCode == 1) {
                var html = $.Mustache.render('user-login-template', {});
                $('#main-content').html(html);
            }
        },
        onLoggingInSuccess: function(response) {
            showMenuBar();
        },
        onLoggingInError: function(response) {
            alert("Email or password entered was wrong. Try again!");
            console.log(response);
        }
    };
    
    var init = function() {
        $.Mustache.addFromDom('user-login-template');
        $.Mustache.addFromDom('user-menu-bar-template');
    };

    var bindElements = function() {
        $('body').delegate('#user-login-submit', 'click', function(e) {
            var data = {
                email: $("#user-email").val(),
                password: $("#user-password").val()
            };
            CommonUtils.makePostCall(CommonUtils.urls.login, data, callbacks.onLoggingInSuccess, callbacks.onLoggingInError);
        });
        $('body').delegate('#companies-menu', 'click', function(e) {
            TWApp.Company.init();
        });
        $('body').delegate('#questions-menu', 'click', function(e) {
            TWApp.Questions.init();
        });
    };
    
    init();
    bindElements();
    CommonUtils.makeGetCall(CommonUtils.urls.isLoggedIn, callbacks.isLoggedInSuccess, callbacks.isLoggedInError);
}();