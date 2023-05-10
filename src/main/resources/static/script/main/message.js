var message = document.querySelector("#message");
var messageContainer = document.querySelector(".message-container");

if(message.innerText != null && message.innerText != "") {
    messageContainer.classList.add("message");
} else {
    messageContainer.classList.remove("message");

}
