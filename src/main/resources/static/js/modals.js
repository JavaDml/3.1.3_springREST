$(document).ready(function () {
//=========Начальная загрузка таблицы===================
    restart();
//======================================================

    let deleteModal = document.getElementById('deleteModal');
    let editModal = document.getElementById('editModal');

//=========Показ Delete-формы===========================
    deleteModal.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget;
        let href = button.getAttribute('data-bs-whatever');
        fetch(href).then((res) => {
            if (res.ok) {
                console.log("Delete-Form is open...");
                console.log(res);
                res.json().then(user => {
                    $('.deleteForm #id1').attr('readonly', 'readonly').val(user.id);
                    $('.deleteForm #username1').attr('readonly', 'readonly').val(user.name);
                    $('.deleteForm #surname1').attr('readonly', 'readonly').val(user.surname);
                    $('.deleteForm #age1').attr('readonly', 'readonly').val(user.age);
                    $('.deleteForm #password1').attr('readonly', 'readonly').val(user.password);
                    $('#roles1 option').prop('selected', false);
                    user.roles.forEach(role => $(`.deleteForm #roles1 option:contains(${role.role})`).prop('selected', true));
                });
            }
        });
    });
//======================================================

//=========Показ Edit-формы=============================
    editModal.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget;
        let href = button.getAttribute('data-bs-whatever');
        fetch(href).then((res) => {
                if (res.ok) {
                    console.log("Edit-Form is open...");
                    console.log(res);
                    res.json().then(user => {
                        $('.editForm #id2').val(user.id);
                        $('.editForm #username2').val(user.name);
                        $('.editForm #surname2').val(user.surname);
                        $('.editForm #age2').val(user.age);
                        $('.editForm #password2').val(user.password);
                        $('.editForm #originalPass').val(user.password);
                        $('#roles2 option').prop('selected', false);
                        user.roles.forEach(role => $(`.editForm #roles2 option:contains(${role.role})`).prop('selected', true));
                    });
                }
        });
    });
//======================================================
});

//====Кнопки Edit и Delete на формах, кнопка NewUser====
document.onclick = function (event) {
    let button = $(event.target);
    switch (button.attr("id")) {
        case "newBtnForm" : {
            event.preventDefault();
            let user = {
                name: $('#usernameNew').val(),
                surname: $('#surnameNew').val(),
                age: $('#ageNew').val(),
                password: $('#passwordNew').val(),
                roles: getRoles("#roles")
            }
            addOrEditUser(user)
            console.log(user);
            break;
        }
        case "deleteBtnForm" : {
            event.preventDefault();
            let user = {
                id: $('#id1').val(),
                name: $('#username1').val(),
                surname: $('#surname1').val(),
                age: $('#age1').val(),
                password: $('#password1').val(),
                roles: getRoles("#roles1")
            }
            delUser(user)
            console.log("User is deleted");
            break;
        }
        case "editBtnForm" : {
            event.preventDefault();
            let user = {
                id: $('#id2').val(),
                name: $('#username2').val(),
                surname: $('#surname2').val(),
                age: $('#age2').val(),
                password: $('#password2').val(),
                roles: getRoles("#roles2")

            }
            addOrEditUser(user)
            console.log(user);
            break;
        }
        default : {
            break;
        }

    }
}
//======================================================

//=========Функция получения данных пользователей=======
function restart() {
    let usersTableBody = $("#usersTable");
    usersTableBody.empty();

    fetch("rest/getAllUsers")
        .then((res) => {
            if (res.ok) {
                console.log("Get resp from rest/getAllUsers...");
                console.log(res);
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
    console.log("Table has been updated...");
    let firstTabEl = document.querySelector('#user-table-tab');
    let firstTab = new bootstrap.Tab(firstTabEl);

    firstTab.show();
}
//======================================================

//=========Функция заполнения таблицы пользователей=====
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
                            data-bs-whatever="/rest/get_user/${user.id}">
                         Edit
                    </button>
                 </td>

                 <td>
                    <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            data-bs-whatever="/rest/get_user/${user.id}">
                        Delete
                    </button>
                 </td>
             </tr>`;
}
//======================================================

//=========Функция получения ролей=================================
function getRoles(address) {
    let select = $(address).val();
    if(select.length == 0) {
        return;
    }
    let roles = [];
    for (let i = 0; i < select.length; i++) {
        roles.push({
            id: select[i],
            role: $(`${address} option[value=${select[i]}]`).text(),
        });
    }
    return roles;
}
//======================================================

//=========Функция Add/Edit=================================
function addOrEditUser(user) {
    if(user.roles == undefined) {
        alert("Choose the roles!");
        return;
    }
    fetch("rest/addOrEdit_user", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
        body: JSON.stringify(user)
    }).then(function (response) {
        $('.editForm #editModal').modal('hide');
        if(response.ok) {
            restart();
        } else {
            alert(response.status);
        };
    })
}
//======================================================

//=========Функция Delete===============================
function delUser(user) {
    console.log(`Deleting user with id=${user.id}...`);
    fetch("rest/del_user", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
        body: JSON.stringify(user)
    }).then(function (response) {
        $('.deleteForm #deleteModal').modal('hide');
        if(response.ok) {
            restart();
        } else {
            alert(response.status);
        };
    });
}
//======================================================

