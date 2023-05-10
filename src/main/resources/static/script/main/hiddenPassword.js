const password = document.querySelector("#password");

const eyeOpen = document.querySelector(".eye-open");


const eyeClose = document.querySelector(".eye-close");

eyeOpen.addEventListener('click', function(){
    eyeOpen.classList.add('hidden');
    eyeClose.classList.remove('hidden');
    password.setAttribute('type', 'password');
})

eyeClose.addEventListener('click', function(){
    eyeClose.classList.add('hidden');
    eyeOpen.classList.remove('hidden');
    password.setAttribute('type', 'text');
})  

password.addEventListener('focus', function(){
    eyeClose.classList.add('black');
    eyeOpen.classList.add('black');
})

password.addEventListener('blur', function(){
    eyeClose.classList.remove('black');
    eyeOpen.classList.remove('black');
})