function deleteExcel() {
    $.post("delete_questions")
        .done(function () {
            alert("Вопросы удалены")
        })
        .fail(function () {
        });
}

function deleteImages() {
    $.post("delete_images")
        .done(function () {
        alert("Изображения удалены")
    })
        .fail(function () {
        });
}

function showList() {
    modal.style.display = "block";
}