window.onscroll = function() {myFunction()}

function myFunction() {
    var header = document.querySelector('.header');
    if(document.body.scrollTop > 150 || document.documentElement.scrollTop > 150) {
        header.classList.add('header-background');
    }
    else {
        header.classList.remove('header-background')   
    }
}


