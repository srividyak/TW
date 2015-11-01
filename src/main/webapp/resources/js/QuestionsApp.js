/**
 * Created by srividyak on 08/07/15.
 */
TWApp = CommonUtils.isUndefinedOrNull(TWApp) ? {} : TWApp;

TWApp.Questions = {
    
    first: true,
    
    screenType: "listQuestions",

    getCompanies: function(callback) {
        var app = this;
        var success = function(response) {
            var companies = response.companies;
            for (var i = 0; i < companies.length; i++) {
                var company = companies[i];
                company["json"] = JSON.stringify(company);
            }
            var data = {
                "companies": response.companies
            };
            if (app.screenType === "listQuestions") {
                data.listQuestions = true;
            } else if (app.screenType === "newQuestion") {
                data.newQuestion = true;
            }
            var html = $.Mustache.render('questions-template', data);
            $('#main-content').html(html);
            if (typeof callback === "function") {
                callback();
            }
        };
        var error = function(response) {
            console.log(response);
            if (typeof callback === "function") {
                callback();
            }
        };
        CommonUtils.makeGetCall(CommonUtils.urls.getCompanies, success, error);
    },
    
    saveQuestion: function(data) {
        var app = TWApp.Questions;
        var success = function(response) {
            alert("Question saved!");
            app.init();
        };
        var error = function(response) {
            console.log(response);
        };
        CommonUtils.makePostCall(CommonUtils.urls.saveQuestions, {questions: JSON.stringify([data])}, success, error);
    },
    
    updateQuestion: function(data) {
        var app = TWApp.Questions;
        var success = function(response) {
            alert("Question is updated!");
            app.init();
        };
        var error = function(response) {
            console.log(response);
        };
        CommonUtils.makePostCall(CommonUtils.urls.editQuestion, {question: JSON.stringify(data)}, success, error);
    },
    
    prepareQuestion: function(questionNode, cb) {
        var industriesStr = questionNode.find('.question-industries').val();
        var industries = industriesStr.split(",");
        var companyId = null;
        if ($('#questions-companies-list option:selected').val() !== "") {
            companyId = $('#questions-companies-list option:selected').val();
        }
        var questionId = null, type = questionNode.find('.question-type option:selected').val();
        if (CommonUtils.isUndefinedOrNull(type)) {
            type = "text";
        }
        if (!CommonUtils.isUndefinedOrNull(questionNode.attr('data-question-id'))) {
            questionId = questionNode.attr('data-question-id');
        }
        var data = {
            tags: questionNode.find('.question-tags').val(),
            industries: industries,
            type: type,
            content: questionNode.find('.question-content').val()
        };
        if (companyId != null) {
            data.companyId = companyId;
        }
        if (questionId != null) {
            data.id = questionId;
        }
        cb(data);
    },
    
    getQuestionsForCompany: function(companyId) {
        var success = function(response) {
            var questionsObject = {
                questions: response.questions
            };
            var html = $.Mustache.render('company-questions-template', questionsObject);
            if ($('#questions-content #company-questions').length > 0) {
                $('#questions-content #company-questions').remove();
            }
            $('#questions-content').append(html);
        };
        var error = function(response) {
            console.log(response);
        };
        var data = {
            company: JSON.stringify({
                id: companyId
            })
        };
        CommonUtils.makePostCall(CommonUtils.urls.getQuestionsForCompany, data,  success, error);
    },
    
    bindElements: function() {
        var app = this;
        $('body').delegate('#add-question-btn', 'click', function(e) {
            var cb = function() {
                var html = $.Mustache.render('add-question-template', {});
                if ($('#questions-content #add-question').length > 0) {
                    $('#questions-content #add-question').remove();
                }
                $('#questions-content').append(html);
            };
            app.screenType = "newQuestion";
            app.getCompanies(cb);
        });
        $('body').delegate('#questions-companies-list', 'change', function(e) {
            var option = $('#questions-companies-list option:selected');
            var companyId = option.val();
            if (app.screenType === "listQuestions") {
                app.getQuestionsForCompany(companyId);
            }
        });
        $('body').delegate('#save-question', 'click', function(e) {
            app.prepareQuestion($('#add-question'), app.saveQuestion);
        });
        $('body').delegate('.save-question', 'click', function(e) {
            var curTarget = $(e.currentTarget);
            app.prepareQuestion(curTarget.closest('.question-item'), app.updateQuestion);
        });
    },

    init: function() {
        this.screenType = "listQuestions";
        if (this.first === true) {
            this.getCompanies();
            $.Mustache.addFromDom('add-question-template');
            $.Mustache.addFromDom('questions-template');
            $.Mustache.addFromDom('company-questions-template');
            this.bindElements();
            this.first = false;
        } else {
            this.getCompanies();
        }
    }
};