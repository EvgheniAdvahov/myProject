$(document).ready(function () {

    var table = $('#example').DataTable({
        scrollX: true,
        buttons: [
            "copy", "csv", "excel", "print", 'colvis'
        ],
        initComplete: function () {
            var filterTable = $('.filter_table'); // Получаем div с классом filter_table
            this.api().columns([1, 2, 3]).every(function () {
                let column = this;

                // Create select element
                let select = document.createElement('select');
                select.add(new Option(''));
                column.footer().replaceChildren(select);

                // Apply listener for user change in value
                select.addEventListener('change', function () {
                    column.search(select.value, {exact: true}).draw();
                });

                $(select).click(function (e) {
                    e.stopPropagation();
                });

                // Add list of options
                column.data().unique().sort().each(function (d, j) {
                    select.add(new Option(d));
                });
                // filterTable.append(select);
            });
        }
    });

    table.buttons().container().appendTo($('.my-buttons'));

});
