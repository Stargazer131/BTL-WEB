var messageErrorContainer = document.querySelector('.message-error-container');
var messageSuccessContainer = document.querySelector('.message-success-container');

var messageErrors = document.querySelectorAll('.message-error');
var messageSuccess = document.querySelector('.message-success');

if (messageSuccess.innerText != null && messageSuccess.innerText != "")
{
    messageSuccessContainer.classList.remove("hidden");
    messageErrorContainer.classList.add("hidden");
    messageSuccessContainer.classList.add("message");
}

function check(messageErrors) {
    ok = false;
    messageErrors.forEach(function(message){
        if (message.innerText != null && message.innerText != "")
        {
            ok = true;
        }
    })
    if (!ok) return false;
    return true;
}

if (check(messageErrors)) {
    messageErrorContainer.classList.remove("hidden");
    messageSuccessContainer.classList.add("hidden");
    messageErrorContainer.classList.add("messageError");
}