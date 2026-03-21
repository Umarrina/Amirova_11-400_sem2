
Пользователь может создавать, редактировать и удалять свои заметки. Заметки бывают публичные и приватные.

Security: /notes/public – доступен всем, /notes/** – только ROLE_USER, /admin/** – только ROLE_ADMIN.

AdminNoteController (@RestController):

GET /admin/notes – все заметки всех пользователей (JSON)
DELETE /admin/notes/{id} – удаление любой заметки
Шаблоны (FreeMarker): notes.ftl, note_form.ftl, public_notes.ftl

Проверить: без логина /notes → редирект, /notes/public → работает; user не может открыть /admin/notes (403); нельзя редактировать чужую заметку

дедлайн: сегодняшний день


public List<NoteDto> getAllNotes() {
return noteRepository.findAll().stream()
.map(this::toDto)
.collect(Collectors.toList());
}

1. зачем вообще нужен дто
2. почеу просто не вернуть список заметток