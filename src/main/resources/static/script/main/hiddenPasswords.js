const passwords = document.querySelectorAll(".password");

const eyeOpens = document.querySelectorAll(".eye-open");


const eyeCloses = document.querySelectorAll(".eye-close");

eyeOpens.forEach(function(eyeOpen,index){
    eyeOpen.addEventListener('click', function(){
        eyeOpen.classList.add('hidden');
        eyeCloses[index].classList.remove('hidden');
        passwords[index].setAttribute('type', 'password');
    })
})

eyeCloses.forEach(function(eyeClose,index){
    eyeClose.addEventListener('click', function(){
        eyeClose.classList.add('hidden');
        eyeOpens[index].classList.remove('hidden');
        passwords[index].setAttribute('type', 'text');
    }) 
})

passwords.forEach(function(password,index){
    password.addEventListener('focus', function(){
        eyeCloses[index].classList.add('black');
        eyeOpens[index].classList.add('black');
    })
})

passwords.forEach(function(password,index){
    password.addEventListener('blur', function(){
        eyeCloses[index].classList.remove('black');
        eyeOpens[index].classList.remove('black');
    })
})

 
