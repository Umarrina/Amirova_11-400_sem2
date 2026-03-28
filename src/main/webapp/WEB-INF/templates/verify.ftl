<html>
<head>
    <title>Подтверждение регистрации</title>
</head>
<body>
<h1>Введите код подтверждения</h1>

<#if error??>
    <div style="color:red">${error}</div>
</#if>

<form action="/verify" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div>
        <label>Код подтверждения:</label><br>
        <input type="text" name="code" placeholder="Введите код" required>
    </div>
    <button type="submit">Подтвердить</button>
</form>
</body>
</html>