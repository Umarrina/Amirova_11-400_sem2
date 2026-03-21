<html>
<head>
    <title>Мои заметки</title>
</head>
<body>
<h1>Мои заметки</h1>
<a href="/notes/create">Создать заметку</a>
<br><br>
<table border="1">
    <tr>
        <th>Заголовок</th>
        <th>Содержание</th>
        <th>Дата</th>
        <th>Публичная</th>
        <th>Действия</th>
    </tr>
    <#list notes as note>
        <tr>
            <td>${note.title}</td>
            <td>${note.content}</td>
            <td>${note.createdAt}</td>
            <td>${note.isPublic?string("да", "нет")}</td>
            <td>
                <a href="/notes/${note.id}/edit">Редактировать</a>
                <form action="/notes/${note.id}/delete" method="post" style="display:inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </#list>
</table>
</body>
</html>