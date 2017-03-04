$(function(){
    $('.input-pre').click(function(){
        $(this).removeClass('input-pre').val("");
    });
    //startLoading();
});

function startLoading(){
    $('#overlay[name="loading-overlay"]').show();
    $('body').addClass('no-overflow');
}

function completeLoading(){
    $('#overlay[name="loading-overlay"]').hide();
    $('body').removeClass('no-overflow');
}