<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
    <spring:url value="/resources/js/jquery.min.js" var="jquery"/>
    <spring:url value="/resources/js/CommonUtils.js" var="common"/>
    <spring:url value="/resources/js/UserApp.js" var="userApp"/>
    <spring:url value="/resources/js/mustache.js" var="mustache"/>
    <spring:url value="/resources/js/jquery.mustache.js" var="jqueryMustache"/>
    <spring:url value="/resources/js/CompanyApp.js" var="company"/>
    <spring:url value="/resources/js/QuestionsApp.js" var="questions"/>
    
    <style>
        body {
            text-align: center;
        }
        #user-menu-bar li {
            display: inline;
            list-style-type: none;
            margin-left: 10px;
        }
        #company-info-container {
            margin-top: 10px;
        }
        #company-info-container #add-company div {
            margin: 10px;
        }
        #add-company-btn {
            margin-left: 10px;
        }
        #user-login-form div, #add-question div {
            margin: 10px;
        }
    </style>
    
</head>
<body>
    <div id="user-menu-bar">
    </div>
    <div id="main-content">
        
    </div>    

    <script type="text/html" id="user-login-template">
        <div id="user-login-form">
            <div>
                <label for="user-email">Email:</label>
                <input type="text" id="user-email"/>    
            </div>
            
            <div>
                <label for="user-password">Password:</label>
                <input type="password" id="user-password"/>    
            </div>
            
            <button id="user-login-submit">Submit</button>
        </div>
    </script>

    <script type="text/html" id="user-menu-bar-template">
            <ul>
                <li><a href="#" id="companies-menu">Companies</a></li>
                <li><a href="#" id="questions-menu">Questions</a></li>
            </ul>
    </script>

    <script type="text/html" id="companies-template">
        <div id="companies-content">
            <select id="companies-list">
                <option data-company-json=""></option>
                {{#companies}}
                    <option data-company-json="{{json}}" value="{{id}}">{{name}}, {{location}}</option>
                {{/companies}}
            </select>
            <a href="#" id="add-company-btn">Add Company</a>
            <div id="company-info-container"></div>
        </div>    
    </script>
    
    <script type="text/html" id="questions-template">
        <div id="questions-content">
            <select id="questions-companies-list">
                <option value="">
                    {{#newQuestion}}
                        Select Company to associate the question
                    {{/newQuestion}}
                    {{#listQuestions}}
                        Select Company to choose questions associated
                    {{/listQuestions}}
                </option>
                {{#companies}}
                    <option value="{{id}}">{{name}}, {{location}}</option>
                {{/companies}}
            </select>
            <a href="#" id="add-question-btn">Add question</a>
        </div>
    </script>
    
    <script type="text/html" id="company-questions-template">
        <table id="company-questions" align="center">
            <tbody>
            {{#questions}}
                <tr class="question-item" data-question-id="{{id}}">
                    <td>
                        <input type="text" class="question-tags" value="{{tags}}"/>
                    </td>
                    <td>
                        <input type="text" class="question-industries" value="{{industries}}"/>
                    </td>
                    <td>
                        <select class="question-type">
                            <option value="TEXT">Text</option>
                            <option value="IMAGE">Image</option>
                            <option value="MULTIPLE_CHOICE_SINGLE_ANSWER">Multiple choice single answer</option>
                            <option value="MULTIPLE_CHOICE_MULTIPLE_ANSWERS">Multiple choice multiple answer</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </td>

                    <td>
                        <input type="text" class="question-content" value="{{content}}"/>
                    </td>

                    <td>
                        <button class="save-question">Save</button>
                    </td>
                </tr>
            {{/questions}}
            </tbody>
        </table>
    </script>

    <script type="text/html" id="add-company-template">
        <div id="add-company">
            <div>
                <label for="company-name">Name: </label>
                <input type="text" value="{{name}}" id="company-name"/>    
            </div>
            
            <div>
                <label for="company-industry">Industry: </label>
                <input type="text" value="{{industry}}" id="company-industry"/>    
            </div>
            
            <div>
                <label for="company-location">Location: </label>
                <input type="text" value="{{location}}" id="company-location"/>    
            </div>
            
            <div>
                <label for="company-numEmployees">Number of Employees: </label>
                <input type="number" value="{{numEmployees}}" id="company-numEmployees"/>
            </div>
            
            <div>
                <label for="company-website">Website: </label>
                <input type="text" value="{{website}}" id="company-website"/>    
            </div>
            {{#newCompany}}
                <button id="save-company">Save</button>
            {{/newCompany}}
        </div>    
    </script>

    <script type="text/html" id="add-question-template">
        <div id="add-question">
            <div>
                <label for="question-tags">Tags (comma separated values): </label>
                <input type="text" id="question-tags" class="question-tags" value="{{tags}}"/>
            </div>
            
            <div>
                <label for="question-industries">Industries (comma separated values): </label>
                <input type="text" id="question-industries" class="question-industries" value="{{industries}}"/>
            </div>
            
            <div>
                <label for="question-type">Type</label>
                <select id="question-type" class="question-type">
                    <option value="TEXT">Text</option>
                    <option value="IMAGE">Image</option>
                    <option value="MULTIPLE_CHOICE_SINGLE_ANSWER">Multiple choice single answer</option>
                    <option value="MULTIPLE_CHOICE_MULTIPLE_ANSWERS">Multiple choice multiple answer</option>
                    <option value="OTHER">Other</option>
                </select>
            </div>
            
            <div>
                <label for="question-content">Content:</label>
                <input type="text" id="question-content" class="question-content"/>
            </div>
            
            <button id="save-question">Save</button>
            
        </div>
    </script>

</body>
<script src="${jquery}"></script>
<script src="${jqueryMustache}"></script>
<script src="${mustache}"></script>
<script src="${common}"></script>
<script src="${company}"></script>
<script src="${questions}"></script>
<script src="${userApp}"></script>
</html>