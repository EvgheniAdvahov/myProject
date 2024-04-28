$(document).ready(function () {

    var table = $('#example').DataTable(
        {
            scrollX: true,
            buttons: [
                "copy", "csv", "excel", "print"
            ]
        }
    );
    table.buttons().container()
        .appendTo( $('.my-buttons' ) );
});

// $(document).ready(function (){
//
//     var table = $('#example').DataTable({
//
//         buttons:['copy', 'csv', 'excel', 'pdf', 'print']
//     });
//
//     table.buttons().container()
//         .appendTo('#example_wrapper .col-md-6:eq(0)');
// });




// var table = $('#example').DataTable( {
//     buttons: [
//         'copy', 'excel', 'pdf'
//     ]
// } );

// table.buttons().container()
//     .appendTo( $('.col-sm-6:eq(0)', table.table().container() ) );