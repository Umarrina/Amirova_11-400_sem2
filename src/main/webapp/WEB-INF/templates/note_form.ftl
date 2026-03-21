<html>
<head>
    <title><#if note.id??>Редактирование<#else>Создание</#if> заметки</title>
</head>
<body>
<h1><#if note.id??>Редактирование<#else>Создание</#if> заметки</h1>
<form action="/notes/${note.id}/edit" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div>
        <label>Заголовок:</label><br>
        <input type="text" name="title" value="${note.title!''}" required>
    </div>
    <div>
        <label>Содержание:</label><br>
        <textarea name="content">${note.content!''}</textarea>
    </div>
    <div>
        <label>Публичная:</label>
        <input type="hidden" name="isPublic" value="false" />
        <input type="checkbox" name="isPublic" value="true" <#if note.isPublic?? && note.isPublic>checked</#if> />
    </div>
    <button type="submit">Сохранить</button>
</form>
<br>
<a href="/notes">Назад к моим заметкам</a>
</body>
</html>