window.onscroll = function() {myFunction()}

function myFunction() {
    var header = document.querySelector('.header');
    if(document.documentElement.scrollTop > 150) {
        header.classList.add('header-background');
    }
    else {
        header.classList.remove('header-background')   
    }
}


