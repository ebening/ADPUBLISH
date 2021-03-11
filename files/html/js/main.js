// Función para Agenda
(function() {
    $('.agenda-body > div:first-child').addClass('active');
    $('.agenda-body > div').click(function() {
        $(this).toggleClass('active');
    });
    $('.agenda-month .agenda-week:first-child > div:first-child').addClass('active');
    $('.agenda-month .agenda-week > div').click(function() {
        $(this).toggleClass('active');
    });

    // Function menú index
    var toggler = $('.toggler-menu'),
        menu = $('.menu-nuevo');

    toggler.click(function(e) {
        menu.slideToggle();
        e.preventDefault;
    });


}());
