$(document).ready(function () {
    fetch("/api/current-user")
        .then(response => response.json())
        .then(user => {
        $("#currentUserEmail").text(user.email);
        // Поскольку роли - это строки, можно использовать replace напрямую
        $("#currentUserRoles").text(user.roles.map(r => r.replace("ROLE_", "")).join(" "));

        let userRow = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(r => r.replace("ROLE_", "")).join(" ")}</td>
                </tr>
            `;
        $("#userTableBody").append(userRow);
    })
        .catch(error => console.error("Ошибка загрузки данных пользователя:", error));
});