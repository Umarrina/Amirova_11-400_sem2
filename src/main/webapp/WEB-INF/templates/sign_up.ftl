<html>
<head>
    <title>Регистрация</title>
</head>
<body>
<h1>Регистрация</h1>

<#if error??>
    <div style="color:red">${error}</div>
</#if>

<form action="/sign_up" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div>
        <label>Имя пользователя:</label><br>
        <input type="text" name="username" placeholder="Имя пользователя" required>
    </div>
    <div>
        <label>Email:</label><br>
        <input type="email" name="email" placeholder="Email" required>
    </div>
    <div>
        <label>Пароль:</label><br>
        <input type="password" name="password" placeholder="Пароль" required>
    </div>
    <button type="submit">Зарегистрироваться</button>
</form>
</body>
</html>