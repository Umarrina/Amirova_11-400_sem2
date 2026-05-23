<!doctype html>
<html>
<head>
    <title>Public Chat History</title>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h2>Last 50 messages</h2>
    <ul>
        <#list messages as msg>
            <li><b>${msg.author.username}:</b> ${msg.content} <small>(${msg.sentAt})</small></li>
        </#list>
    </ul>
    <a href="/chat">Go to live chat (login required)</a>
</div>
</body>
</html>