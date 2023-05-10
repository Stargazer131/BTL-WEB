const icon = document.querySelector('.icon');
const inputFile = document.querySelector('input[type="file"]');
const imageBox = document.querySelector('.image');

icon.addEventListener('click', function(){
    inputFile.click();
});

inputFile.addEventListener('change', function(){    
   const file = this.files[0];
   showImage(file);
});

function showImage(file) {
    const fileReader = new FileReader();
    fileReader.onload = function() {
       const fileURL = fileReader.result;
        const imgTag = `<img src="${fileURL}" width="200" height="280">`;
        imageBox.innerHTML = imgTag;
    }
    fileReader.readAsDataURL(file);
}
