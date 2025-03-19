$(document).ready(function () {
    loadUsers();
    loadCurrentUser();
    getAuthenticatedUser();

    $(document).ready(function () {
        fetch("/api/user")
            .then(response => response.json())
            .then(user => {
            // Проверяем, если у пользователя НЕТ роли ADMIN, отправляем его на user.html
            if (!user.roles.some(role => role.name === "ROLE_ADMIN")) {
                window.location.href = "/user.html";
            }
        })
            .catch(error => console.error("Ошибка загрузки данных пользователя:", error));
    });


    // Загружаем список пользователей
    function loadUsers() {
        $.ajax({
            url: '/api/users',
            method: 'GET',
            dataType: 'json',
            success: function (users) {
                let tableBody = $('#users tbody');
                tableBody.empty();
                users.forEach(user => {
                    let roles = user.roles.join(', ');
                    let userRow = `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${roles}</td>
                        <td>
                            <button class="btn btn-info edit-btn" data-id="${user.id}" data-toggle="modal" data-target="#editModal">Edit</button>
                        </td>
                        <td>
                            <button class="btn btn-danger delete-btn" data-id="${user.id}" data-toggle="modal" data-target="#deleteModal">Delete</button>
                        </td>
                    </tr>`;
                    tableBody.append(userRow);
                });
            },
            error: function () {
                alert('Ошибка загрузки пользователей.');
            }
        });
    }

    // Получаем данные авторизованного пользователя
    function getAuthenticatedUser() {
        $.ajax({
            url: '/api/current-user',
            method: 'GET',
            dataType: 'json',
            success: function (user) {
                $('#currentUserEmail').text(user.email);
                $('#currentUserRoles').text(user.roles.join(', '));
            }
        });
    }

    function loadCurrentUser() {
        $.ajax({
            url: '/api/current-user',
            method: 'GET',
            dataType: 'json',
            success: function (user) {
                $('#currentUserEmail').text(user.email);
                $('#currentUserRoles').text(user.roles.join(', '));

                let tableBody = $('#userTable tbody');
                tableBody.empty();
                let roles = user.roles.join(', ');
                let userRow = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${roles}</td>
                </tr>`;
                tableBody.append(userRow);
            },
            error: function () {
                alert('Ошибка загрузки данных пользователя.');
            }
        });
    }

    // Открытие модального окна для редактирования
    $(document).on('click', '.edit-btn', function () {
        const userId = $(this).data('id');
        $.ajax({
            url: '/api/users/' + userId,
            method: 'GET',
            dataType: 'json',
            success: function (user) {
                $('#editId').val(user.id);
                $('#editEmail').val(user.email);
                $('#editFirstName').val(user.name);
                $('#editLastName').val(user.lastName);
                $('#editAge').val(user.age);
                $('#editRoles').val(user.roles);
                $('#editPassword').val(''); // поле пароля очищается
            }
        });
    });

    // Обработчик сохранения изменений при редактировании
    $('#editUserForm').on('submit', function (e) {
        e.preventDefault();
        const userId = $('#editId').val();
        const userData = {
            email: $('#editEmail').val(),
            name: $('#editFirstName').val(),
            lastName: $('#editLastName').val(),
            age: $('#editAge').val(),
            roles: $('#editRoles').val(),
            password: $('#editPassword').val() ? $('#editPassword').val() : undefined // пароль отправляется, только если он заполнен
        };
        $.ajax({
            url: '/api/users/' + userId,
            method: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function () {
                loadUsers();
                $('#editModal').modal('hide');
            },
            error: function () {
                alert('Ошибка при редактировании пользователя.');
            }
        });
    });

    // Обработчик добавления пользователя
    $('#addUserForm').on('submit', function (e) {
        e.preventDefault();
        const userData = {
            email: $('#addEmail').val(),
            name: $('#addFirstName').val(),
            lastName: $('#addLastName').val(),
            age: $('#addAge').val(),
            roles: $('#addRoles').val(),
            password: $('#addPassword').val() // пароль отправляется, даже если поле пустое
        };
        $.ajax({
            url: '/api/users',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function () {
                loadUsers();
                $('#addUserModal').modal('hide');
            },
            error: function () {
                alert('Ошибка при добавлении пользователя.');
            }
        });
    });

    // Открытие модального окна для удаления
    $(document).on('click', '.delete-btn', function () {
        const userId = $(this).data('id');
        $('#deleteUserId').val(userId); // передаем id пользователя в скрытое поле
    });

    // Обработчик удаления пользователя
    $('#deleteUserForm').on('submit', function (e) {
        e.preventDefault();
        const userId = $('#deleteUserId').val();

        if (!confirm('Вы уверены, что хотите удалить этого пользователя?')) {
            return;
        }

        $.ajax({
            url: '/api/delete/' + userId,
            method: 'DELETE',
            success: function () {
                loadUsers();
                $('#deleteModal').modal('hide');
            },
            error: function () {
                alert('Ошибка при удалении пользователя.');
            }
        });
    });
});
