$(document).ready(function () {

    var table = $('#example').DataTable(
        {
            scrollX: true,
            buttons: [
                "copy", "csv", "excel", "print", 'colvis'
            ]
        }
    );
    table.buttons().container()
        .appendTo( $('.my-buttons' ) );
});
