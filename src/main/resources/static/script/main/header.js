window.onscroll = function() {myFunction()}

function myFunction() {
    var header = document.querySelector('.header');
    if(document.body.scrollTop > 300 || document.documentElement.scrollTop > 300) {
        header.classList.add('header-background');
    }
    else {
        header.classList.remove('header-background')   
    }
}

