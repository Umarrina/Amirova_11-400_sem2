<html>
<head>
    <title>Регистрация</title>
</head>
<body>
<h1>Регистрация</h1>
</body>
</html>

<#if error??>
<div style="color:red">${error}</div>
</#if>

<form action="/sign_up" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="text" name="username" placeholder="Имя пользователя" required>
    <input type="password" name="password" placeholder="Пароль" required>
    <button type="submit">Зарегистрироваться</button>
</form>