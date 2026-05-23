<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Live Chat</title>
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h2>Chat</h2>
    <div class="form-group">
        <input type="text" id="messageInput" class="form-control" placeholder="Your message...">
        <button id="sendBtn" class="btn btn-primary mt-2">Send</button>
    </div>
    <div id="messages" class="mt-4">
        <#list messages as msg>
            <div><b>${msg.author.username}:</b> ${msg.content} <small>(${msg.sentAt})</small></div>
        </#list>
    </div>
</div>

<script>
    let stompClient = null;

    $(function() {
        connect();
        $("#sendBtn").click(function() {
            let content = $("#messageInput").val().trim();
            if (content && stompClient) {
                stompClient.send("/app/send", {}, content);
                $("#messageInput").val("");
            }
        });
    });

    function connect() {
        let socket = new SockJS("/ws");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log("Connected: " + frame);
            stompClient.subscribe("/topic/messages", function(message) {
                let msg = JSON.parse(message.body);
                $("#messages").append("<div><b>" + msg.authorName + ":</b> " + msg.content + " <small>(" + msg.sentAt + ")</small></div>");
            });
        });
    }
</script>
</body>
</html>