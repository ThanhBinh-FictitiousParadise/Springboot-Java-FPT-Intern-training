$(document).ready(function () {
    $('#studentsTable').DataTable({
        "order": [
            [4, "asc"], // This here sort students by Status (column 4), Ascending
            [0, "asc"]   // Then by ID (column 0), Ascending, pretty cool eh?
        ]
    });

$('#subjectsTable').DataTable({
    "order": [
        [4, "asc"],   // Sort subjects by ID (column 0), Ascending
        [0, "asc"]]
    });
});