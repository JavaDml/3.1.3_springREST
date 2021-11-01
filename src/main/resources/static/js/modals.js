$(document).ready(function () {
    restart();

    let deleteModal = document.getElementById('deleteModal');
    let editModal = document.getElementById('editModal');

    deleteModal.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget;
        let href = button.getAttribute('data-bs-whatever');
        $.get(href, function (user) {
            $('.deleteForm #id1').attr('readonly', 'readonly').val(user.id);
            $('.deleteForm #username1').attr('readonly', 'readonly').val(user.name);
            $('.deleteForm #surname1').attr('readonly', 'readonly').val(user.surname);
            $('.deleteForm #age1').attr('readonly', 'readonly').val(user.age);
            $('.deleteForm #roles1').empty();
            user.roles.forEach(role => $('.deleteForm #roles1').append(new Option(role.role)));
            $('.deleteForm #deleteFormBody').attr('action', '/admin/del_user/' + (user.id));
        });
    });

    editModal.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget;
        let href = button.getAttribute('data-bs-whatever');
        $.get(href, function (user) {
            $('.editForm #id2').val(user.id);
            $('.editForm #username2').val(user.name);
            $('.editForm #surname2').val(user.surname);
            $('.editForm #age2').val(user.age);
            $('.editForm #password2').val(user.password);
            $('.editForm #originalPass').val(user.password);
            $('.editForm #roles2 option:first').prop('selected', false);
            $('.editForm #roles2 option:last').prop('selected', false);
            user.roles.forEach(role => {
                if (role.role == "USER_ROLE") {
                    $('.editForm #roles2 option[value=USER_ROLE]').prop('selected', true);
                }
                if (role.role == "USER_ADMIN") {
                    $('.editForm #roles2 option[value=USER_ADMIN]').prop('selected', true);
                }
            });
            $('.editForm #editFormBody').attr('action', '/admin/addOrEdit_user');
        });
    });
});

function restart() {
    let usersTableBody = $("#usersTable");
    usersTableBody.empty();

    fetch("rest/getAllUsers")
        .then((res) => {
            if (res.ok) {
                console.log(res)
                res.json().then(dataTable => dataTable.forEach(function (user) {
                    usersTableBody.append(tableRow(user));
                }));
            } else {
                alert("Ошибка HTTP: " + res.status);
            }
        }).catch(error => {
        console.log(error);
        alert("Ошибка HTTP: " + error);
    });
}

function tableRow(user) {

    return `<tr>
                 <td>${user.id}</td>
                 <td>${user.name}</td>
                 <td>${user.surname}</td>
                 <td>${user.age}</td>
                 <td>${user.rolesToString}</td>

                 <td>
                    <button type="button" class="btn btn-info" data-bs-toggle="modal"
                            data-bs-target="#editModal"
                            th:data-bs-whatever="@{/rest/get_user/{id} (id=${user.id}) }">
                         Edit
                    </button>
                 </td>

                 <td>
                    <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            th:data-bs-whatever="@{/rest/get_user/{id} (id=${user.id}) }">
                        Delete
                    </button>
                 </td>
             </tr>`;
}

