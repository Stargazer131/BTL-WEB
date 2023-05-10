var queries = document.querySelectorAll(".color");

queries.forEach(function(query){
    queries.forEach(function(query){
        if(query.innerText == "Trả đúng hạn" || query.innerText == "Đang mượn") {
            query.classList.add("green");
        }
        
        if(query.innerText == "Quá hạn" || query.innerText == "Trả muộn")
        {
            query.classList.add("red");
        }
        
        if(query.innerText == "Yêu cầu mượn")
        {
            query.innerText = "Đang duyệt yêu cầu mượn";
            query.classList.add("yellow");
        }
        
        if(query.innerText == "Yêu cầu trả")
        {
            query.innerText = "Đang duyệt yêu cầu trả";
            query.classList.add("orange");
        }
    })
})

