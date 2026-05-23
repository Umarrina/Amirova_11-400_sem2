<!doctype html>
<html>
<head>
    <title>My Messages</title>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h2>My messages</h2>
    <table class="table">
        <tr>
            <th>Content</th>
            <th>Sent at</th>
            <th>Action</th>
        </tr>
        <#list messages as msg>
            <tr>
                <td>${msg.content}</td>
                <td>${msg.sentAt}</td>
                <td>
                    <form action="/chat/${msg.id}/delete" method="post">
                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                    </form>
                </td>
            </tr>
        </#list>
    </table>
</div>
</body>
</html>