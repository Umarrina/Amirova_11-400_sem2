<html>
<head>
    <title>Публичные заметки</title>
</head>
<body>
<h1>Публичные заметки</h1>
<table border="1">
    <tr>
        <th>Заголовок</th>
        <th>Содержание</th>
        <th>Дата</th>
        <th>Автор</th>
    </tr>
    <#list notes as note>
        <tr>
            <td>${note.title}</td>
            <td>${note.content}</td>
            <td>${note.createdAt}</td>
            <td>${note.author.username}</td>
        </tr>
    </#list>
</table>
<br>
<a href="/">На главную</a>
</body>
</html>